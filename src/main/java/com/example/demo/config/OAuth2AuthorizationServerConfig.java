package com.example.demo.config;

import com.example.demo.config.oauth.OAuthClientDetailService;
import com.example.demo.helper.LessStrictRedirectUriAuthorizationCodeTokenGranter;
import com.example.demo.login.MyResourceOwnerPasswordTokenGranter;
import com.example.demo.security.authorization.MyUserApprovalHandler;
import com.example.demo.token.MyTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig implements AuthorizationServerConfigurer {
    private static final String RESOURCE_ID = "blog_resource";


    @Autowired
    private MyUserApprovalHandler userApprovalHandler;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.authenticationEntryPoint(customEntryPoint());
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.userApprovalHandler()
        endpoints.userApprovalHandler(userApprovalHandler)
                .requestFactory(oAuth2RequestFactory())
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(tokenServices())
                .tokenEnhancer(osiamTokenEnhancer())
                .tokenGranter(tokenGranter());
//        endpoints.tokenStore(tokenStore());
//        endpoints.authenticationManager(authenticationManager());
//        endpoints.clientDetailsService(clientDetailsService());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

    @Bean
    public OAuth2AuthenticationEntryPoint customEntryPoint() {
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setRealmName("springsec/client");
        oAuth2AuthenticationEntryPoint.setTypeName("Basic");
        return oAuth2AuthenticationEntryPoint;
    }


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        //client details and token service,
        //each client can be configured specifically with permissions to be able to use certain authorization mechanisms and access grants
        OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        oAuth2AuthenticationManager.setTokenServices(tokenServices);
        return oAuth2AuthenticationManager;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new OAuthClientDetailService();
    }

    @Bean
    public ClientDetailsUserDetailsService clientDetailsUserDetailsService() {
        return new ClientDetailsUserDetailsService(clientDetailsService());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(clientDetailsUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public OAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService());
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public DefaultTokenServices tokenServices() throws Exception {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService());
        tokenServices.setTokenEnhancer(osiamTokenEnhancer());
        tokenServices.afterPropertiesSet();
        return tokenServices;
    }

    @Bean
    public TokenEnhancer osiamTokenEnhancer() {
        return new MyTokenEnhancer();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public TokenGranter tokenGranter() throws Exception {
        return new CompositeTokenGranter(Arrays.asList(new TokenGranter[]{
                new ClientCredentialsTokenGranter(
                        tokenServices(), clientDetailsService(), oAuth2RequestFactory()
                ),
                new MyResourceOwnerPasswordTokenGranter(
                        authenticationManager(), tokenServices(), clientDetailsService(), oAuth2RequestFactory()
                ),
                new RefreshTokenGranter(
                        tokenServices(), clientDetailsService(), oAuth2RequestFactory()
                ),
                new LessStrictRedirectUriAuthorizationCodeTokenGranter(
                        tokenServices(), authorizationCodeServices(), clientDetailsService(), oAuth2RequestFactory()
                )
        }));
    }


}

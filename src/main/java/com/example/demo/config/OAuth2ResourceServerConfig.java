package com.example.demo.config;

import com.example.demo.security.authorization.MyMethodSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private DefaultTokenServices tokenService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("oauth2res")
                .tokenServices(tokenService)
                .expressionHandler(new MyMethodSecurityExpressionHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/ServiceProviderConfig").permitAll()
                .antMatchers("/management/**").access("#oauth2.hasScope('ADMIN')")
                .antMatchers("/Me/**").access("#oauth2.hasScope('ADMIN') or #oauth2.hasScope('ME')")
                .antMatchers(HttpMethod.DELETE.POST, "/Users/**").access("#oauth2.hasScope('ADMIN')")
                .regexMatchers(HttpMethod.GET, "/Users/?").access("#oauth2.hasScope('ADMIN')")
                .antMatchers("/Users/**")
                .access("#oauth2.hasScope('ADMIN') or #oauth2.hasScope('ME') and #osiam.isOwnerOfResource()")
                .antMatchers("/token/validation").authenticated()
                .antMatchers("/token/revocation", "/token/revocation/")
                .access("#oauth2.hasScope('ADMIN') or #oauth2.hasScope('ME')")
                .antMatchers("/token/revocation/**")
                .access("#oauth2.hasScope('ADMIN') or #oauth2.hasScope('ME') and #osiam.isOwnerOfResource()")
                .anyRequest().access("#oauth2.hasScope('ADMIN')");
        // @formatter:on
    }
}

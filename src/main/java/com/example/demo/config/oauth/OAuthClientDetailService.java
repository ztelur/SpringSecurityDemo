package com.example.demo.config.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OAuthClientDetailService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(clientId);
        details.setAuthorizedGrantTypes(Arrays.asList("authorization_code") );
        details.setScope(Arrays.asList("read, trust"));
        details.setResourceIds(Arrays.asList("oauth2-resource"));
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        details.setAuthorities(authorities);
        return details;
    }
}

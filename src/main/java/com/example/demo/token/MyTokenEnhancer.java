package com.example.demo.token;

import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class MyTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("expires_at", token.getExpiration());

        if (token.getRefreshToken() != null) {
            DefaultExpiringOAuth2RefreshToken refreshToken =
                    (DefaultExpiringOAuth2RefreshToken) token.getRefreshToken();
            additionalInformation.put("refresh_token_expires_at", refreshToken.getExpiration());
        }

        additionalInformation.put("client_id", authentication.getOAuth2Request().getClientId());

//        if (authentication.getUserAuthentication() != null && authentication.getPrincipal() instanceof User) {
//            User user = (User) authentication.getPrincipal();
//            additionalInformation.put("user_name", user.getUserName());
//            additionalInformation.put("user_id", user.getId());
//        }

        token.setAdditionalInformation(additionalInformation);

        return accessToken;
    }
}

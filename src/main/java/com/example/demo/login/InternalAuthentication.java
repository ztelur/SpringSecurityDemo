package com.example.demo.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class InternalAuthentication extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 5020697919339414782L;

    public InternalAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public InternalAuthentication(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

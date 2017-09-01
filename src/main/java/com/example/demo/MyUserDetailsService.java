package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(SecureResourceFilterInvocationDefinitionSource.class);
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("loadUserByUsername " + s);
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();


        GrantedAuthority auth1 = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority auth2 = new SimpleGrantedAuthority("ROLE_USER");


        if (s.equals("user")) {
            auths.add(auth1);
        } else if (s.equals("admin")) {
            auths.add(auth2);
        }
        //得到用户的权限
//        auths = pubUsersHome.loadUserAuthoritiesByName( username );

        String password = "p";

        //取得用户的密码
//        password = pubUsersHome.getPasswordByUsername( username );
        return new User(s, password, true, true, true, true, auths);
    }
}

package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(SecurityAccessDecisionManager.class);
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal add auth");
        String auth = httpServletRequest.getHeader("Authorization");
        //用户登陆，暂不设置权限
//        Token token = new Token(auth, null);
        //TODO:auth并且可以添加权限信息到请求上去。
        Object token = new Object();
        Authentication authentication = new UsernamePasswordAuthenticationToken(auth, token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

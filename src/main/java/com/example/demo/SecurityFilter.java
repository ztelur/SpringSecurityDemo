package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

public class SecurityFilter extends AbstractSecurityInterceptor implements Filter {
    private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);


    @Autowired
    SecureResourceFilterInvocationDefinitionSource invocationSource;

    @Autowired
    SecurityAccessDecisionManager decisionManager;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostConstruct
    public void init(){
        super.setAccessDecisionManager(decisionManager);
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init in Security ");
    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("doFilter in Security ");
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        //before invocation会调用SecureResourceDataSource中的逻辑
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return invocationSource;
    }


}

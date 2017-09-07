package com.example.demo.security.authorization;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;

public class MyMethodSecurityExpressionHandler extends OAuth2WebSecurityExpressionHandler {
    public MyMethodSecurityExpressionHandler() {
        super();
    }

    public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
                                                                     FilterInvocation filterInvocation) {
        StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, filterInvocation);
//        ec.setVariable("osiam", new OsiamSecurityExpressionMethods(authentication, filterInvocation));
        return ec;
    }
}

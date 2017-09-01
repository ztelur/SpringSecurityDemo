package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class CustomPermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration {

}

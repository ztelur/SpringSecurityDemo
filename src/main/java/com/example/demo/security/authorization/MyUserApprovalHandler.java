package com.example.demo.security.authorization;

import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.stereotype.Component;

@Component
public class MyUserApprovalHandler extends DefaultUserApprovalHandler {

}

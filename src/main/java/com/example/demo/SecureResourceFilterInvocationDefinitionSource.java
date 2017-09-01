package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;
@Component
public class SecureResourceFilterInvocationDefinitionSource implements FilterInvocationSecurityMetadataSource, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(SecureResourceFilterInvocationDefinitionSource.class);

    private PathMatcher matcher;

    private static Map<String, Collection<ConfigAttribute>> map = new HashMap<>();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        logger.info("getAttributes");
        //TODO:应该做instanceof
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String method = filterInvocation.getHttpRequest().getMethod();
        String requestURI = filterInvocation.getRequestUrl();
        //
        //循环资源路径，当访问的Url和资源路径url匹配时，返回该Url所需要的权限
        for(Iterator<Map.Entry<String, Collection<ConfigAttribute>>> iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Collection<ConfigAttribute>> entry = iter.next();
            String url = entry.getKey();

            if(matcher.match(url, requestURI)) {
                return map.get(requestURI);
            }
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("afterPropertiesSet");
        this.matcher = new AntPathMatcher();//用来匹配访问资源路径
        Collection<ConfigAttribute> atts = new ArrayList<>();
        //TODO:可以有多个权限，这些权限就可以是||也可以是&&的关系吧？
        ConfigAttribute ca = new SecurityConfig("ROLE_ADMIN");
        atts.add(ca);
        //TODO:如果是restful api 如何将权限和http method进行匹配
        map.put("/announcements/add", atts);
        Collection<ConfigAttribute> attsno =new ArrayList<ConfigAttribute>();
        ConfigAttribute cano = new SecurityConfig("ROLE_USER");
        ConfigAttribute cano2 = new SecurityConfig("ROLE_ADMIN");
        attsno.add(cano);
        attsno.add(cano2);
        map.put("/announcements/query", attsno);
    }
}

package com.example.demo.config;

import com.example.demo.MyUserDetailsService;
import com.example.demo.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityWebMvcConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    AccessDeniedHandler accessDeniedHandler;
//    @Autowired
//    AuthTokenFilter authTokenFilter;


    @Bean
    UserDetailsService customUserService() {
        return new MyUserDetailsService();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//        http.authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated()
//                .and()
                //TODO:添加filter时，要设置顺序，或者该filter本来就有默认的order,可以查看 https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html
//                .antMatcher("/announcements").addFilterAt(new SecurityFilter(), FilterSecurityInterceptor.class)
//                .antMatcher("/affair").addFilterAt(new SecurityFilter(), FilterSecurityInterceptor.class).exceptionHandling().accessDeniedHandler(accessDeniedHandler);
//        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)

//          http
//            .addFilterBefore(securityFilter(), FilterSecurityInterceptor.class)
//                .csrf().disable()
//                //不需要session,基于token
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


//        http
//                .authorizeRequests()
//                .antMatchers("/css/**", "/index").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .and()
//                .formLogin().loginPage("/login").failureUrl("/login-error");

//        super.configure(http);

        //.userDetailsService(userDetailsService)
        http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
//                .and()
//                .antMatcher("/announcements").addFilterAfter(securityFilter(), FilterSecurityInterceptor.class).authorizeRequests().and()
//                .csrf().disable()
////                不需要session,基于token
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("p").roles("USER")
                .and()
                .withUser("admin").password("p").roles("ADMIN");
    }

    //这样才能全部自动装配
    @Bean
    public SecurityFilter securityFilter() throws Exception
    {
        return new SecurityFilter();
    }

}

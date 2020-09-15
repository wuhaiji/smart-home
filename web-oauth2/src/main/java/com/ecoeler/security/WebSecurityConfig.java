package com.ecoeler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 开启WebSecurity
 * 启用Spring Security 注解，基于方法的注解
 *
 * @author tang
 * @since 2020/9/7
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * UserDetailsService
     */
    @Autowired
    private UserDetailsService myUserDetailService;

    /**
     * 配置安全拦截机制
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//防止跨站请求伪造，限制除了get以外的大多数方法
                //配置路径拦截，表明路径访问所对应的权限，角色，认证信息（具体的规则放在最前排声明）
                .authorizeRequests()
                    //.antMatchers("/oauth/**").permitAll()
                    //.antMatchers("/admin/**").hasAuthority("show")//访问 /admin/** 必须要有ADMIN角色
                    //.antMatchers("/test/**").hasAuthority("p1")//访问 /test/** 必须要有p1权限
                    //.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                    .anyRequest().authenticated() //除此之外的请求，都需要认证
            .and()

                .formLogin()
                    //如果不指定loginPage，会使用security自带的登录页面，且地址为/login
                    //.loginPage("/login")//指定登录的地址为login，没有认证的用户会跳转到这个页面
                    //.loginProcessingUrl("/login-handler")//处理登录请求的URL，不需要自行实现，spring security已经实现了
                    .successForwardUrl("/login-success");
    }


    /**
     * 查找的用户的密码会和用户登录输入的密码相比较
     * 密码编码器，来定义比对方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(myUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

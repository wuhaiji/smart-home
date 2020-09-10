package com.ecoeler.app.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * security
 * 开启WebSecurity
 * 启用Spring Security 注解，基于方法的注解
 * @author tang
 * @since 2020/9/10
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置安全拦截机制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                //允许跨域！！！
                .and().csrf().disable()
                //防止跨站请求伪造，限制除了get以外的大多数方法
                //配置路径拦截，表明路径访问所对应的权限，角色，认证信息（具体的规则放在最前排声明）
                .authorizeRequests()
                //除此之外的请求，都需要认证
                    .anyRequest().authenticated();
    }

}

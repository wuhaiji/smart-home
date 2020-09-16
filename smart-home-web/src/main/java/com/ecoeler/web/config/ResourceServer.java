package com.ecoeler.web.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //登录
                .antMatchers("/open/**").permitAll()

                .anyRequest().authenticated()
                //关闭csrf
                .and()
                .csrf().disable()
                //因为是基于token的方式，因此session就不用再记录了
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}

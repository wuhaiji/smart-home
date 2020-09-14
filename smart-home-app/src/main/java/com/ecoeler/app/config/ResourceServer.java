package com.ecoeler.app.config;


import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/app_user/login").permitAll()
                //令牌的scope
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                //关闭csrf
                .and().csrf().disable()
                //因为是基于token的方式，因此session就不用再记录了
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

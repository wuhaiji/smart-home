package com.ecoeler.app.config;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Configuration
@EnableResourceServer
@Slf4j
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Autowired
    AppResourceProperties appResourceProperties;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 用于请求oauth 权限的工具，负载均衡
     *
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });
        return restTemplate;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl(appResourceProperties.getCheckTokenUrl());
        service.setClientId(appResourceProperties.getClientId());
        service.setClientSecret(appResourceProperties.getClientSecret());
        service.setRestTemplate(restTemplate);
        resources.resourceId(appResourceProperties.getResourceId())
                //用来解决匿名用户 访问无权限资源时的异常 无TOKEN或TOKEN无效也是
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.getWriter().write(JSONObject.toJSONString(Result.error(TangCode.CODE_TOKEN_ERROR)));
                })
                .tokenServices(service)
                .stateless(true)
        ;
    }


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

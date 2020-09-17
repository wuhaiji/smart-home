package com.ecoeler.web.config;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Value("${spring.oauth2.client-id}")
    private String clientId;

    @Value("${spring.oauth2.resource-id}")
    private String resourceId;

    @Value("${spring.oauth2.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth2.check-token-url}")
    private String checkTokenUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 用于请求oauth 权限的工具，负载均衡
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
        RemoteTokenServices service=new RemoteTokenServices();
        service.setCheckTokenEndpointUrl(checkTokenUrl);
        service.setClientId(clientId);
        service.setClientSecret(clientSecret);
        service.setRestTemplate(restTemplate);
        resources.resourceId(resourceId)
                //用来解决匿名用户 访问无权限资源时的异常 无TOKEN或TOKEN无效也是
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setStatus(HttpStatus.OK.value());
                        response.setHeader("Content-Type", "application/json;charset=UTF-8");
                        response.getWriter().write(JSONObject.toJSONString(Result.error(TangCode.CODE_TOKEN_ERROR)));
                    }
                })
                //用来解决认证过的用户 访问无权限资源时的异常，会被 ControllerAdvice 全局异常处理 所捕获
//                .accessDeniedHandler(new AccessDeniedHandler() {
//                    @Override
//                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                        response.setStatus(HttpStatus.OK.value());
//                        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//                        response.getWriter().write(JSONObject.toJSONString(Result.error(TangCode.CODE_NO_AUTH_ERROR)));
//                    }
//                })
                .tokenServices(service);
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

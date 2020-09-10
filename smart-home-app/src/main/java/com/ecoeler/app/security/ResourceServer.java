package com.ecoeler.app.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * 资源服务
 * @author tang
 * @since 2020/9/9
 */
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

    // 当资源服务 和 令牌服务不在同一个工程时
    @Bean
    public ResourceServerTokenServices tokenService(){
        RemoteTokenServices service=new RemoteTokenServices();
        service.setCheckTokenEndpointUrl(checkTokenUrl);
        service.setClientId(clientId);
        service.setClientSecret(clientSecret);
        return service;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId)
                .tokenServices(tokenService());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1/**").permitAll()
                //令牌的scope
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                //关闭csrf
                .and().csrf().disable()
                //因为是基于token的方式，因此session就不用再记录了
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

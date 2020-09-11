package com.ecoeler.app;


import com.ecoeler.annotation.EnableSmartHomeApi;
import com.ecoeler.common.EnableApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * APP
 *
 * @author tang
 * @since 2020/9/10
 */

@SpringBootApplication
@EnableOAuth2Sso
@EnableSmartHomeApi
public class SmartHomeAppApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeAppApp.class,args);
    }
}

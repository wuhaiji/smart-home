package com.ecoeler.app;


import com.ecoeler.annotation.EnableAppOauth2Client;
import com.ecoeler.annotation.EnableSmartHomeApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@EnableOAuth2Client
@EnableAppOauth2Client
@EnableSmartHomeApi
@EnableDiscoveryClient
@SpringBootApplication
public class SmartHomeAppApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeAppApp.class,args);
    }
}

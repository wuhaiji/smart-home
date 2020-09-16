package com.ecoeler.web;

import com.ecoeler.annotation.EnableSmartHomeApi;
import com.ecoeler.annotation.EnableWebOauth2Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@EnableOAuth2Client
@EnableWebOauth2Client
@EnableSmartHomeApi
@EnableDiscoveryClient
@SpringBootApplication
public class SmartHomeWebApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeWebApp.class,args);
    }
}

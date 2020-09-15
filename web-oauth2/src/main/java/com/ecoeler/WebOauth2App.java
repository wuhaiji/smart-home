package com.ecoeler;

import com.ecoeler.annotation.EnableSmartHomeApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * APP
 * @author tang
 * @since 2020/9/7
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableSmartHomeApi
@SpringBootApplication
public class WebOauth2App {
    public static void main(String [] args){
        SpringApplication.run(WebOauth2App.class,args);
    }
}

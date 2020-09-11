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
public class AppOauth2App {
    public static void main(String [] args){
        SpringApplication.run(AppOauth2App.class,args);
    }
}

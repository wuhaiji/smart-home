package com.ecoeler;

import com.ecoeler.annotation.EnableAppOauth2Client;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@EnableAsync
@MapperScan("com.ecoeler.app.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableAppOauth2Client
public class SmartHomeServiceApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeServiceApp.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

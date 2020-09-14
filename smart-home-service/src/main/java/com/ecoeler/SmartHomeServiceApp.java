package com.ecoeler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@EnableAsync
@MapperScan("com.ecoeler.app.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class SmartHomeServiceApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeServiceApp.class,args);
    }
}

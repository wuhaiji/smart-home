package com.ecoeler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@MapperScan("com.ecoeler.app.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class SmartHomeServiceApp {
    public static void main(String [] args){
        SpringApplication.run(SmartHomeServiceApp.class,args);
    }
}

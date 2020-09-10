package com.test;

import com.ecoeler.annotation.EnableSmartHomeApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * APP
 * @author tang
 * @since 2020/9/7
 */
@SpringBootApplication
@EnableSmartHomeApi
public class App {
    public static void main(String [] args){
        SpringApplication.run(App.class,args);
    }
}

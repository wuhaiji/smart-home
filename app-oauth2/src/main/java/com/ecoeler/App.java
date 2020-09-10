package com.ecoeler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * APP
 * @author tang
 * @since 2020/9/7
 */
@MapperScan("com.ecoeler.mapper")
@SpringBootApplication
public class App {
    public static void main(String [] args){
        SpringApplication.run(App.class,args);
    }
}

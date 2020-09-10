package com.ecoeler.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * APP
 * @author tang
 * @since 2020/9/10
 */
@EnableOAuth2Sso
@SpringBootApplication
public class App {
    public static void main(String [] args){
        SpringApplication.run(App.class,args);
    }
}

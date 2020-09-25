package com.ecoeler.voice.google;


import com.ecoeler.annotation.EnableAppOauth2Client;
import com.ecoeler.annotation.EnableSmartHomeApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * google语音接入
 *
 * @author whj
 * @since 2020/9/22
 */
@EnableOAuth2Client
@EnableAppOauth2Client
@EnableSmartHomeApi
@EnableDiscoveryClient
@SpringBootApplication
public class SmartHomeVoiceGoogleApp {
    public static void main(String[] args) {
        SpringApplication.run(SmartHomeVoiceGoogleApp.class, args);
    }
}

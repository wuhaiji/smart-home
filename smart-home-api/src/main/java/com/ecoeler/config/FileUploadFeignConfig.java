package com.ecoeler.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * @author Administrator
 */
@Configuration
public class FileUploadFeignConfig {
    /**
     * 连接超时时间10s
     */
    public static int connectTimeOutMillis = 10000;
    /**
     * 超时时间10s
     */
    public static int readTimeOutMillis = 10000;
    @Bean
    @Primary
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
    }


}

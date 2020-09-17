package com.ecoeler.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;
import feign.Response;
import org.springframework.context.annotation.Primary;


/**
 * feign配置类
 * @author tangcx
 */
@Slf4j
@Configuration
public class FeignConfig implements feign.codec.ErrorDecoder{


    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("status:-------" + response.status());
        return feign.FeignException.errorStatus(methodKey, response);
    }
    @Primary
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(3 * 1000, 3 * 1000);
    }
}

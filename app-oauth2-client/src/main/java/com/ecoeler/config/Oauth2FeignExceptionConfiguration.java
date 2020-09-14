package com.ecoeler.config;


import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class Oauth2FeignExceptionConfiguration {

    @Bean
    public ErrorDecoder errorDecoder(){
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                return null;
            }
        };
    }


}

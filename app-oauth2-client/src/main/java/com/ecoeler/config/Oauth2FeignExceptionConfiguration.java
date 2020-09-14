package com.ecoeler.config;


import com.ecoeler.code.Oauth2ClientCode;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

/**
 * oauth2 feign异常
 * @author tang
 * @since 2020/9/14
 */
public class Oauth2FeignExceptionConfiguration {

    @Bean
    public ErrorDecoder errorDecoder(){
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                if(response.status()== 400 ){
                    return new ServiceException(Oauth2ClientCode.CODE_BAD_CREDENTIALS);
                }else{
                    return new ServiceException(CommonCode.SERVER_ERROR);
                }

            }
        };
    }


}

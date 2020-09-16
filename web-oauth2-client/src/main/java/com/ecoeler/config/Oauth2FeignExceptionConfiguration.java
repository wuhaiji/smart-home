package com.ecoeler.config;



import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.TangCode;
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
                    return new ServiceException(TangCode.CODE_LOGIN_ERROR);
                }else{
                    return new ServiceException(CommonCode.SERVER_ERROR);
                }

            }
        };
    }


}

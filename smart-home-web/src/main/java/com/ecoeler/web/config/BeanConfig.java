package com.ecoeler.web.config;

import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.TangCode;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class BeanConfig {



}

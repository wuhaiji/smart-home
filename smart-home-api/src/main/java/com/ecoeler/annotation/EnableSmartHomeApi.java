package com.ecoeler.annotation;


import com.ecoeler.common.ApiRegister;
import com.ecoeler.common.EnableApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 开启API
 * @author tang
 * @since 2020/9/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ApiRegister.class})
@EnableApi("com.ecoeler")
@EnableFeignClients("com.ecoeler.feign")
public @interface EnableSmartHomeApi {

}

package com.ecoeler.feign;

import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * app user fiegn
 * @author tang
 * @since 2020/9/10
 */
@FeignClient(value = "blbs-service-control", path = "/app_user")
public interface AppUserFiegnClient {

    /**
     * 获取图片验证码
     * @param email
     * @return
     */
    @PostMapping("/captcha")
    Result<String> captcha(String email) ;

    /**
     * 登录验证码验证
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/verify")
    Result verify(String email, String code);

}

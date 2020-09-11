package com.ecoeler.feign;

import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "app-oauth2", path = "/oauth")
public class Oauth2ClientService {

//    /**
//     * 获取图片验证码
//     * @param email
//     * @return
//     */
//    @PostMapping("/captcha")
//    Result<String> captcha(@RequestParam String email) ;
//
//    /**
//     * 登录验证码验证
//     * @param email
//     * @param code
//     * @return
//     */
//    @PostMapping("/verify")
//    Result verify(@RequestParam String email,@RequestParam String code);

}

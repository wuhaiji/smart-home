package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * app user feign
 * @author tang
 * @since 2020/9/10
 */
@FeignClient(value = "smart-home-service", path = "/app-user",contextId = "app-user")
public interface AppUserService {

    /**
     * 获取图片验证码
     * @param email
     * @return
     */
    @PostMapping("/captcha")
    Result captcha(@RequestParam String email) ;

    /**
     * 登录验证码验证
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/verify")
    Result verify(@RequestParam String email,@RequestParam String code);

    /**
     * 根据email查询用户
     * @param email
     * @return
     */
    @PostMapping("/user")
    Result<AppUser> getUser(@RequestParam String email);

    /**
     * 发送邮箱验证码
     * @param email
     * @param ip
     * @return
     */
    @PostMapping("/email_code")
    Result sendCode(@RequestParam String email,@RequestParam String ip);

    /**
     * 注册用户
     * @param appUser
     * @param emailCode
     * @return
     */
    @PostMapping("/register")
    Result register(@RequestBody AppUser appUser, @RequestParam String emailCode);

    @PostMapping("/leave/family")
    Result dissolveFamily(@RequestBody UserFamilyDto userFamilyDto);

    @PostMapping("/dissolve/family")
    Result leaveFamily(@RequestBody UserFamilyDto userFamilyDto);
}

package com.ecoeler.controller;

import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.response.Result;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AuthController {

    @Resource
    ConsumerTokenServices consumerTokenServices;


    @PostMapping("/oauth/customize/logout")
    public Result Logout(@RequestParam String accessToken) {
        if (accessToken.contains("Bearer")) {
            accessToken = accessToken.substring(7);
        }
        boolean b = consumerTokenServices.revokeToken(accessToken);
        if (b) {
            return Result.ok();
        }
        return Result.error(CommonCode.LOGOUT_FAILED);
    }

}
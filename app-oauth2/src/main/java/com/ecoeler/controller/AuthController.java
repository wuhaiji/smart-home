package com.ecoeler.controller;

import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.response.Result;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

@FrameworkEndpoint
public class AuthController {

    @Resource
    ConsumerTokenServices consumerTokenServices;


    @PostMapping("/oauth2/customize/logout")
    public Result Logout(String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return Result.ok();
        }
        return Result.error(CommonCode.LOGOUT_FAILED);
    }

}
package com.ecoeler.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.feign.AppUserService;
import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.request.OauthPasswordRequest;
import com.ecoeler.model.response.Result;

import com.ecoeler.util.Oauth2PasswordRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 用户登录
 * @author tang
 * @since 2020/9/10
 */
@Slf4j
@RequestMapping("/app_user")
@RestController
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @Autowired
    private Oauth2PasswordRequestFactory oauth2PasswordRequestFactory;



    @PreAuthorize("isAnonymous()")
    @RequestMapping("/login")
    public Result login(String email,String password){
        OauthPasswordRequest build = oauth2PasswordRequestFactory.build(email, password);
        log.info(JSONObject.toJSONString(
                oauth2ClientService.getTokenByPasswordModel(
                        build
                )
        ));
        return Result.ok();
    }

}

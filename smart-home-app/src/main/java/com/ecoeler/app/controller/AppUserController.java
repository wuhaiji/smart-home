package com.ecoeler.app.controller;

import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.response.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @PreAuthorize("isAnonymous()")
    @RequestMapping("/login")
    public Result login(String email,String password){

        log.info(oauth2ClientService.getTokenByPasswordModel(clientId,clientSecret,email,password));

        return Result.ok();
    }

}

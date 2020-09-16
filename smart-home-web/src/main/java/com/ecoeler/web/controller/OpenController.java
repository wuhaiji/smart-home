package com.ecoeler.web.controller;

import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 开放 端口
 * @author tang
 * @since 2020/9/16
 */
@RequestMapping("/open")
@RestController
public class OpenController {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private Oauth2ClientService oauth2ClientService;


    @RequestMapping("/login")
    public Result login(String account, String password){
        ExceptionUtil.notBlank(account, TangCode.CODE_EMAIL_EMPTY_ERROR);
        ExceptionUtil.notBlank(password,TangCode.CODE_PASSWORD_EMPTY_ERROR);
        return Result.ok(oauth2ClientService.getToken(clientId,clientSecret,account,password));
    }

    @RequestMapping("/refresh_token")
    public Result refreshToken(String refreshToken){
        ExceptionUtil.notBlank(refreshToken,TangCode.CODE_REFRESH_TOKEN_EMPTY_ERROR);
        return Result.ok(oauth2ClientService.refreshToken(clientId,clientSecret,refreshToken));
    }



}

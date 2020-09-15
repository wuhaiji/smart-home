package com.ecoeler.app.controller;

import com.ecoeler.app.entity.AppUser;
import com.ecoeler.feign.AppUserService;
import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;

import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 开放接口
 * @author tang
 * @since 2020/9/10
 */
@Slf4j
@RequestMapping("/open")
@RestController
public class OpenController {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @Autowired
    private AppUserService appUserService;

    @RequestMapping("/login")
    public Result login(String email,String password,String code){
        ExceptionUtil.notBlank(email, TangCode.CODE_EMAIL_EMPTY_ERROR);
        ExceptionUtil.notBlank(password,TangCode.CODE_PASSWORD_EMPTY_ERROR);
        ExceptionUtil.notBlank(code , TangCode.CODE_CODE_EMPTY_ERROR);
        appUserService.verify(email,code);
        return Result.ok(oauth2ClientService.getToken(clientId,clientSecret,email,password));
    }

    @RequestMapping("/refresh_token")
    public Result refreshToken(String refreshToken){
        ExceptionUtil.notBlank(refreshToken,TangCode.CODE_REFRESH_TOKEN_EMPTY_ERROR);
        return Result.ok(oauth2ClientService.refreshToken(clientId,clientSecret,refreshToken));
    }

    @RequestMapping("/captcha")
    public String captcha(String email){
        ExceptionUtil.notBlank(email, TangCode.CODE_EMAIL_EMPTY_ERROR);
        return appUserService.captcha(email);
    }

    @PostMapping("/register")
    public Result register(AppUser appUser,String emailCode){
        ExceptionUtil.notBlank(appUser.getUsername(), TangCode.CODE_USERNAME_EMPTY_ERROR);
        ExceptionUtil.notBlank(appUser.getEmail(), TangCode.CODE_EMAIL_EMPTY_ERROR);
        ExceptionUtil.notBlank(appUser.getPassword(),TangCode.CODE_PASSWORD_EMPTY_ERROR);
        ExceptionUtil.notBlank(emailCode , TangCode.CODE_CODE_EMPTY_ERROR);
        return appUserService.register(appUser,emailCode);
    }

    @PostMapping("/email_code")
    public Result sendCode(String email, HttpServletRequest request){
        ExceptionUtil.notBlank(email, TangCode.CODE_EMAIL_EMPTY_ERROR);
        return appUserService.sendCode(email,request.getRemoteAddr());
    }

}

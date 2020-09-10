package com.ecoeler.controller;

import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.utils.HutoolCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * APP用户控制器
 * @author tang
 * @since 2020/9/10
 */
@RequestMapping("/app_user")
@RestController
public class AppUserController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private HutoolCaptchaUtil hutoolCaptchaUtil;

    /**
     * 图片验证码
     * @param email
     * @return
     */
    @PostMapping("/image_captcha")
    public String captcha(String email) {
        return hutoolCaptchaUtil.getCaptchaImage(email);
    }

    @PostMapping("/login")
    public String login(String email,String password){
        if(hutoolCaptchaUtil.verify(email,password);

    }



    @PostMapping
    public String register(AppUser appUser){

    }


}

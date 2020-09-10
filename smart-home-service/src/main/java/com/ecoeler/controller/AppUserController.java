package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.code.AppUserCode;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.utils.HutoolCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping("/captcha")
    public String captcha(@RequestParam String email) {
        return hutoolCaptchaUtil.getCaptchaImage(email);
    }

    @PostMapping("/verify")
    public Result verify(@RequestParam String email, @RequestParam String code){
        if(hutoolCaptchaUtil.verify(email,code)){
            return Result.ok();
        }
        return Result.error(AppUserCode.CODE_CAPTCHA_ERROR);
    }

    @PostMapping
    public String register(AppUser appUser){
        return null;
    }


}
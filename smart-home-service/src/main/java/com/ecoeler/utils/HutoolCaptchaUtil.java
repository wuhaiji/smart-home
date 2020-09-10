package com.ecoeler.utils;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基于hutool 分布式验证码
 * @author tang
 * @since 2020/9/10
 */
@Component
public class HutoolCaptchaUtil {


    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取图片验证码
     * @param username 用户名
     * @return 返回验证码图片的base64字符串
     */
    public String getCaptchaImage(String username){

        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);

        redisUtil.setEx("APP_CAPTCHA:"+username, JSONObject.toJSONString(shearCaptcha),5L, TimeUnit.MINUTES);

        return shearCaptcha.getImageBase64Data();

    }


    public boolean verify(String username , String code){

        ShearCaptcha shearCaptcha = JSONObject.parseObject(redisUtil.get("APP_CAPTCHA:" + username), ShearCaptcha.class);

        //无论验证成功失败，都删除验证码
        redisUtil.delete("APP_CAPTCHA:" + username);

        return shearCaptcha.verify(code);
    }


}

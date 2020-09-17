package com.ecoeler.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.config.AppResourceProperties;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app/voice")
@Slf4j
public class AppVoiceController {


    @Autowired
    AppVoiceService appVoiceService;

    @Autowired
    AppResourceProperties appResourceProperties;

    @RequestMapping(value = "/action")
    public JSONObject action(@RequestBody JSONObject data, OAuth2Authentication authentication, HttpServletRequest request) {

        String userId = (String) authentication.getPrincipal();
        String accessToken = request.getHeader(AppVoiceConstant.DTO_KEY_AUTHORIZATION);

        authentication.getUserAuthentication();
        log.info("user id:{}", userId);
        log.info("access_token:{}", JSON.toJSONString(authentication));
        //获取请求头
        Object details = authentication.getDetails();
        data.put(AppVoiceConstant.DTO_KEY_USER_ID, userId);
        data.put(AppVoiceConstant.DTO_KEY_AUTHORIZATION, accessToken);
        String result = appVoiceService.action(data);
        return JSONObject.parseObject(result);
    }

}

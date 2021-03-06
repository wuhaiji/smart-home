package com.ecoeler.voice.alexa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.service.AppVoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/voice")
@Slf4j
public class AppVoiceController {

    @Autowired
    AppVoiceService appVoiceService;

    @PostMapping(value = "/alexa/action")
    public JSONObject alexaAction(@RequestBody JSONObject requestJson, OAuth2Authentication authentication, HttpServletRequest request) {
        JSONObject data = requestJson;
        data.put(AppVoiceConstant.CLIENT_NAME, AppVoiceConstant.ALEXA_CLIENT);
        String userId = (String) authentication.getPrincipal();
        String accessToken = request.getHeader(AppVoiceConstant.HEADER_AUTHORIZATION);
        if (accessToken.startsWith("Bearer")) {
            accessToken = accessToken.substring(7);
        }

        log.info("user id:{}", userId);
        log.info("access_token:{}", JSON.toJSONString(authentication));
        data.put(AppVoiceConstant.DTO_KEY_USER_ID, userId);
        data.put(AppVoiceConstant.DTO_KEY_AUTHORIZATION, accessToken);
        String action = appVoiceService.action(data);
        log.info("response:{}",action);
        return JSONObject.parseObject(action);
    }

}

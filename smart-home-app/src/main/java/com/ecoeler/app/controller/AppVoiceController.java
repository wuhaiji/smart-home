package com.ecoeler.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/app/voice")
@Slf4j
public class AppVoiceController {


    @Autowired
    AppVoiceService appVoiceService;

    @RequestMapping(value = "/action")
    public JSONObject googleAction(@RequestBody JSONObject data, Authentication authentication, HttpServletRequest request) {
        String principal = (String) authentication.getPrincipal();
        log.info("client id:{}", principal);
        log.info("authentication:{}", authentication);
        //获取请求头
        data.put(AppVoiceConstant.DTO_KEY_USER_ID, authentication.getName());
        data.put(AppVoiceConstant.DTO_KEY_CLIENT_ID, principal);
        data.put(AppVoiceConstant.DTO_KEY_AUTHORIZATION, request.getHeader(AppVoiceConstant.DTO_KEY_AUTHORIZATION));
        String result = appVoiceService.action(data);
        return JSONObject.parseObject(result);
    }
}

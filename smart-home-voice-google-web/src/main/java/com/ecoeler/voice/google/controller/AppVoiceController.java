package com.ecoeler.voice.google.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.voice.google.util.PrincipalUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/voice")
@Slf4j
public class AppVoiceController {

    @Autowired
    AppVoiceService appVoiceService;

    @PostMapping(value = "/google/action")
    public JSONObject googleAction(@RequestBody JSONObject data, HttpServletRequest request) {
        data.put(AppVoiceConstant.CLIENT_NAME, AppVoiceConstant.GOOGLE_CLIENT);
        String accessToken = request.getHeader(AppVoiceConstant.HEADER_AUTHORIZATION);
        if (accessToken.startsWith("Bearer")) {
            accessToken = accessToken.substring(7);
        }
        Long userId = PrincipalUtil.getUserId();
        data.put(AppVoiceConstant.DTO_KEY_USER_ID, userId);
        data.put(AppVoiceConstant.DTO_KEY_AUTHORIZATION, accessToken);

        String action;
        try {
            action = appVoiceService.action(data);
        } catch (Exception e) {
            log.error("Abnormal execution of google voice command：", e);
            String requestId = data.getString("requestId");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", requestId);
            JSONObject payload = new JSONObject();
            payload.put("errorCode", "hardError ");
            payload.put("status", "ERROR ");
            jsonObject.put("payload", payload);
            action = jsonObject.toJSONString();
        }

        return JSONObject.parseObject(action);
    }

}

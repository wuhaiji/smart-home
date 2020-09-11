package com.ecoeler.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/app/voice")
@Slf4j
public class AppVoiceController {


    @Autowired
    AppVoiceService appVoiceService;

    @RequestMapping(value = "/action")
    public JSONObject googleAction(@RequestBody JSONObject data, Authentication authentication, HttpServletRequest request) {
        if (data == null) {
            return new JSONObject();
        }
        //获取请求头
        Map<String, String> headerMap = RequestUtils.getHeaderMap(request);
        headerMap.put("userId", authentication.getName());
        String result = appVoiceService.action(data, headerMap);
        return JSONObject.parseObject(result);
    }
}

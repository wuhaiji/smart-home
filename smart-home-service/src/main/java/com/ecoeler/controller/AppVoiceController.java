package com.ecoeler.controller;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.action.alexa.AlexaAction;
import com.ecoeler.action.google.GoogleAction;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/voice")
public class AppVoiceController {

    @Autowired
    GoogleAction googleAction;

    @Autowired
    AlexaAction alexaAction;

    @PostMapping("/action")
    public Result<String> action(@RequestBody JSONObject data) {

        String clientName = data.getString(AppVoiceConstant.CLIENT_NAME);

        String response;
        switch (clientName) {
            case AppVoiceConstant.GOOGLE_CLIENT:
                response = googleAction.action(data);
                break;
            case AppVoiceConstant.ALEXA_CLIENT:
                response = alexaAction.action(data);
                break;
            default:
                response = "{\"error\":\"invalid client\"}";
        }
        return Result.ok(response);
    }

    @PostMapping("/request/sync")
    public Result<String> requestSync(@RequestBody UserVoiceDto dto) {
        googleAction.requestSync(dto.getUserId());
        return Result.ok();
    }

}

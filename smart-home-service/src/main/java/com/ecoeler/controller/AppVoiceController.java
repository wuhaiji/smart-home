package com.ecoeler.controller;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.action.AlexaAction;
import com.ecoeler.action.GoogleAction;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/voice")
public class AppVoiceController {

    private static final String GOOGLE_CLIENT = "google_client";
    private static final String ALEXA_CLIENT = "alexa_client";

    @Autowired
    GoogleAction googleAction;

    @Autowired
    AlexaAction alexaAction;

    @PostMapping("/action")
    public Result action(JSONObject data) {

        String clientId = data.getString(AppVoiceConstant.DTO_KEY_CLIENT_ID);

        String response;
        switch (clientId) {
            case GOOGLE_CLIENT:
                response = googleAction.action(data);
                break;
            case ALEXA_CLIENT:
                response = alexaAction.action(data);
                break;
            default:
                response = "{\"error\":\"invalid client id\"}";
        }
        return Result.ok(response);
    }

}

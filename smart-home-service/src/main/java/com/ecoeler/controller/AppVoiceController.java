package com.ecoeler.controller;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.action.AlexaAction;
import com.ecoeler.action.GoogleAction;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
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
    public Result action(@RequestBody JSONObject data) {
        String response = googleAction.action(data);
        return Result.ok(response);
    }

}

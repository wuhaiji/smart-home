package com.ecoeler.action.alexa;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.action.alexa.service.AlexaService;
import com.ecoeler.action.alexa.service.OauthService;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlexaAction {

    @Autowired
    AlexaService alexaService;

    @Autowired
    OauthService oauthService;

    public String action(JSONObject data) {
        JSONObject directive = data.getJSONObject("directive");
        JSONObject header = directive.getJSONObject("header");
        JSONObject payload = directive.getJSONObject("payload");
        String namespace = header.getString("namespace");

        Long userId = data.getLong(AppVoiceConstant.DTO_KEY_USER_ID);
        //客户的授权码
        String code = null;
        //客户的访问令牌
        if (payload.containsKey("grant")) {
            JSONObject grant = (JSONObject) payload.get("grant");
            code = grant.getString("code");
        }

        log.info("Found " + namespace + " Namespace");
        AlexaResponse alexaResponse = null;
        switch (namespace) {
            case "Alexa":
                //状态报告
                AlexaResponse alexaResponse1 = alexaResponse = alexaService.StateReport(data);
                break;

            case "Alexa.Authorization":
                //向alexa发送网关事件，主要是事件验证使用
                //获取token令牌信息
                alexaResponse = oauthService.oauthToken(code,userId);
                break;

            case "Alexa.Discovery":
                //发现设备的命令
                alexaResponse = alexaService.Discovery(data );
                break;

            case "Alexa.PowerController":
                //开关设备的指令
                alexaResponse = alexaService.powerController(data);
                break;
            case "Alexa.BrightnessController":
                //开关设备的指令
                alexaResponse = alexaService.brightnessController(data);
                break;

            default:
                System.out.println("INVALID Namespace");
//                alexaResponse = new AlexaResponse();
                break;

        }
        log.info("finally response：" + alexaResponse.toString());
        System.out.println("EDN" +
                "=========================================================================================");
        return alexaResponse.toString();
    }
}

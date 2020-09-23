package com.ecoeler.action.alexa.service;

import com.alibaba.fastjson.JSONObject;

import com.ecoeler.action.alexa.AlexaResponse;

/**
 * @author whj
 * @createTime 2020-02-28 17:14
 * @description
 **/
public interface AlexaService {

    AlexaResponse StateReport(JSONObject data );

    AlexaResponse Discovery(JSONObject data);

    AlexaResponse powerController(JSONObject data);


    AlexaResponse brightnessController(JSONObject data);
}

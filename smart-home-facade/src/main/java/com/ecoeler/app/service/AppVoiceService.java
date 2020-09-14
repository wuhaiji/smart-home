package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface AppVoiceService {
    /**
     * 语音请求action
     *
     * @param data      请求数据
     * @return
     */
    String action(JSONObject data);
}

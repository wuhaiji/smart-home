package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface AppVoiceService {
    /**
     * 语音请求action
     *
     * @param data      请求数据
     * @param headerMap 请求头map
     * @return
     */
    String action(JSONObject data, Map headerMap);
}

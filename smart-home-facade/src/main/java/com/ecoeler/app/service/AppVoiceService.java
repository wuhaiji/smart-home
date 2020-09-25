package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;


public interface AppVoiceService {
    /**
     * 语音请求action
     *
     * @param data      请求数据
     * @return
     */
    String action(JSONObject data);

    /**
     *  请求同步
     * @param dto
     * @return
     */
    void requestSync(UserVoiceDto dto);
}

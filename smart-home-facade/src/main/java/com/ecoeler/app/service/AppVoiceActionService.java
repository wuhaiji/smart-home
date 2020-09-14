package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.UserDto;

import java.util.List;

public interface AppVoiceActionService {
    /**
     * 根据用户ID查询用户的设备列表
     * @param userDto
     * @return
     */
    List<DeviceVoiceBean> getDeviceVoiceBeans(UserDto userDto);
}

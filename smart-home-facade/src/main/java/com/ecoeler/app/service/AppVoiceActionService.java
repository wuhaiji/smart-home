package com.ecoeler.app.service;

import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;

import java.util.List;
import java.util.Map;

public interface AppVoiceActionService {
    /**
     * 根据用户ID查询用户的设备列表
     * @param userVoiceDto
     * @return
     */
    List<DeviceVoiceBean> getDeviceVoiceBeans(UserVoiceDto userVoiceDto);

    /**
     *
     * @param userVoiceDto
     * @return
     */
    Map<String, Object> getUserDeviceStates(DeviceVoiceDto userVoiceDto);
}

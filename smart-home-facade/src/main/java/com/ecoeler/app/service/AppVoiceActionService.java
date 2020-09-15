package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;

import java.util.List;

public interface AppVoiceActionService {
    /**
     * 根据用户ID查询用户的设备列表
     *
     * @param deviceVoiceDto
     * @return
     */
    List<DeviceVoiceBean> getDeviceVoiceBeans(UserVoiceDto deviceVoiceDto);

    /**
     * 根据deviceId获取设备状态信息
     * @param deviceVoiceDto
     * @return
     */
    DeviceInfo getDeviceStates(DeviceVoiceDto deviceVoiceDto);

    /**
     * 根据设备id获取设备类型
     * @param setDeviceId
     * @return
     */
    DeviceType getDeviceType(DeviceVoiceDto setDeviceId);
}

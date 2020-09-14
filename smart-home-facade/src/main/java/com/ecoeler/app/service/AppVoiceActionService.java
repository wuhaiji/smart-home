package com.ecoeler.app.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.Device;

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
     * @param deviceVoiceDto
     * @return
     */
    DeviceInfo getUserDeviceStates(DeviceVoiceDto deviceVoiceDto);

    /**
     * 查询设备信息
     *
     * @param deviceVoiceDto
     * @return
     */
    Device getDeviceNetState(DeviceVoiceDto deviceVoiceDto);

    String DEVICE_STATE_BEANS = "deviceStateBeans";

    String ONLINE = "online";
}

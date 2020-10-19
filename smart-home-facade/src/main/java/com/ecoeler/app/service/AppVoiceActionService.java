package com.ecoeler.app.service;

import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.DeviceSwitchVoiceDto;
import com.ecoeler.app.dto.v1.voice.*;
import com.ecoeler.app.entity.*;

import java.util.List;
import java.util.Map;

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
     *
     * @param deviceId
     * @return
     */
    DeviceInfo getDeviceStatesByDeviceId(String deviceId);

    /**
     * 将deviceInfo转为google需要的states
     * @param deviceId
     * @return
     */
    Map<String, Object> getDeviceGoogleStatesMap(String deviceId);

    /**
     * 按照设备id查询设备的可控key
     *
     * @param deviceId
     * @return
     */
    List<DeviceKey> getDeviceAbleControlKey(String deviceId);

    /**
     * 按条件查询设备key
     *
     * @param dto 条件
     * @return
     */
    List<DeviceKey> getDeviceKeyList(DeviceKeyVoiceDto dto);

    /**
     * 查询deviceType 集合
     *
     * @param dto
     * @return
     */
    List<DeviceType> getDeviceTypeList(DeviceTypeVoiceDto dto);

    /**
     * 根据deviceIds获取设备状态信息
     *
     * @return
     */
    List<DeviceStateBean> getDeviceKeysByIds(List<String> deviceIds);


    /**
     * 根据条件获取设备data 集合
     *
     * @param dto
     * @return
     */
    List<DeviceData> getDeviceDataList(DeviceDataVoiceDto dto);

    /**
     * 查询设备
     *
     * @param dto 条件
     * @return
     */
    Device getDevice(DeviceVoiceDto dto);


    /**
     * 查询device_switch
     *
     * @param dto
     * @return
     */
    List<DeviceSwitch> getDeviceSwitchList(DeviceSwitchVoiceDto dto);

    /**
     * 查询设备列表
     *
     * @param dto 条件
     * @return
     */
    List<Device> getDeviceList(DeviceVoiceDto dto);


    /**
     * 查询device type
     *
     * @param dto
     * @return
     */
    DeviceType getDeviceType(DeviceTypeVoiceDto dto);


    /**
     * 获取设备开关量 集合
     *
     * @param deviceId
     */

    List<DeviceSwitch> getDeviceSwitchListByDeviceId(String deviceId);
}

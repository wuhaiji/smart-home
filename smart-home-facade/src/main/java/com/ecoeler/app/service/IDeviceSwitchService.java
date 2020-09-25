package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.dto.v1.DeviceControlDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceSwitch;

import java.util.List;

/**
 * <p>
 * 设备开关状态表 服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
public interface IDeviceSwitchService extends IService<DeviceSwitch> {

    void openSwitch(DeviceControlDto deviceControlDto);

    void closeSwitch(DeviceControlDto deviceControlDto);

    List<Device> getDeviceList(DeviceControlDto deviceControlDto);

    List<DeviceSwitch> getDeviceSwitchList(String productId);
}

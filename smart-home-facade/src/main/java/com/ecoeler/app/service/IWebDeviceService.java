package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.*;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.entity.Family;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tangCX
 * @since 2020-09-15
 */
public interface IWebDeviceService extends IService<Device> {

    List<Family> selectMap();

    PageBean<Device> selectDevice(WebDeviceDto webDeviceDto, Page<Device> page);

    List<DeviceType> selectDeviceType();

    Long addDevice(Device device);

    void updateDevice(Device device);

    void deleteDevice(Long id);

    List<WebDeviceDataBean> queryDeviceData(Long deviceId);
}

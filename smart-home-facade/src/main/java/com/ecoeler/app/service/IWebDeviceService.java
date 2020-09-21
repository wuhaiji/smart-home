package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.*;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.entity.Family;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tangCX
 * @since 2020-09-15
 */
public interface IWebDeviceService extends IService<Device> {
    /**
     * 查询所有用户家庭的Map
     *
     * @return
     */
    List<Family> selectMap();

    /**
     * 分页按条件查询设备列表
     *
     * @param webDeviceDto 查询条件
     * @return 设备列表
     */
    PageBean<Device> selectDeviceList(WebDeviceDto webDeviceDto);

    /**
     * 查询设备类型
     *
     * @return 设备类型列表
     * @param webDeviceTypeDto
     */
    PageBean<DeviceType> selectDeviceType(WebDeviceTypeDto webDeviceTypeDto);

    /**
     * 新增设备
     *
     * @param device 设备数据
     * @return 新增id
     */
    Long addDevice(Device device);

    /**
     * 修改设备
     *
     * @param device 设备数据
     */
    void updateDevice(Device device);

    /**
     * 删除设备
     *
     * @param id 设备id
     */
    void deleteDevice(Long id);

    /**
     * 查询当前设备对应的设备参数
     *
     * @param deviceId 设备id
     * @return 当前设备设备参数列表
     */
    List<WebDeviceDataBean> queryDeviceData(Long deviceId);



}

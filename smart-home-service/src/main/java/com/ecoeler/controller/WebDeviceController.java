package com.ecoeler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.bean.v1.*;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.service.IWebCustomerService;
import com.ecoeler.app.service.IWebDeviceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * web客户控制器
 *
 * @author tangCX
 * @since 2020/9/15
 */
@Slf4j
@RequestMapping("/web_device")
@RestController
public class WebDeviceController {
    @Autowired
    private IWebDeviceService iWebDeviceService;

    /**
     * 查询设备地图
     * @return
     */
    @RequestMapping("/query/map")
    public Result queryMap() {
        log.info("开始设备地图列表");
        List<Family> result = iWebDeviceService.selectMap();
        return Result.ok(result);
    }

    /***
     * 分页按条件查询设备
     * @param webDeviceDto 查询条件
     * @param page 分页
     * @return 设备列表
     */
    @RequestMapping("/query/device")
    public Result queryDevice(WebDeviceDto webDeviceDto,Page<Device> page) {
        log.info("开始查询设备列表");
        PageBean<Device> result = iWebDeviceService.selectDevice(webDeviceDto,page);
        return Result.ok(result);
    }
    /***
     * 查询所有设备类型
     * @return 设备类型列表
     */
    @RequestMapping("/query/device/type")
    public Result queryDeviceType() {
        log.info("开始查询设备列表");
        List<DeviceType> result = iWebDeviceService.selectDeviceType();
        return Result.ok(result);
    }
    /***
     * 新增设备
     * @return 新增设备id
     */
    @RequestMapping("/save")
    public Result saveDevice(Device device) {
        log.info("开始新增设备");
        Long result = iWebDeviceService.addDevice(device);
        return Result.ok(result);
    }
    /***
     * 修改设备
     * @return 修改设备id
     */
    @RequestMapping("/update")
    public Result updateDevice(Device device) {
        log.info("开始修改设备");
        iWebDeviceService.updateDevice(device);
        return Result.ok();
    }
    /***
     * 删除设备
     * @return 删除设备id
     */
    @RequestMapping("/delete")
    public Result deleteDevice(Long id) {
        log.info("开始删除设备");
        iWebDeviceService.deleteDevice(id);
        return Result.ok();
    }
    /***
     * 查询设备参数
     * @return 删除设备id
     */
    @RequestMapping("/query/data")
    public Result queryDeviceData(Long deviceId) {
        log.info("开始删除设备");
        List<WebDeviceDataBean> result=iWebDeviceService.queryDeviceData(deviceId);
        return Result.ok(result);
    }


}

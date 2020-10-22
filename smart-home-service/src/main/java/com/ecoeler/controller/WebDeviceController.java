package com.ecoeler.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.bean.v1.*;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.service.IWebDeviceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
     *
     * @return
     */
    @RequestMapping("/query/map")
    public Result queryMap() {
        log.info("smart-home-service->WebDeviceController->begin query map");
        List<Family> result = iWebDeviceService.selectMap();
        return Result.ok(result);
    }

    /***
     * 分页按条件查询设备
     * @param webDeviceDto 查询条件
     * @return 设备列表
     */
    @RequestMapping("/query/device/list")
    public Result queryDevice(@RequestBody WebDeviceDto webDeviceDto) {
        log.info("smart-home-service->WebDeviceController->begin query device list");
        PageBean<Device> result = iWebDeviceService.selectDeviceList(webDeviceDto);
        return Result.ok(result);
    }




    /***
     * 查询所有设备类型
     * @return 设备类型列表
     */
    @RequestMapping("/query/all/device/type")
    public Result queryAllDeviceTypeList() {
        log.info("smart-home-service->WebDeviceController->begin query all device type list");
        List<DeviceType> result=iWebDeviceService.selectAllDeviceType();
        return Result.ok(result);
    }

}

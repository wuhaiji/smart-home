package com.ecoeler.web.controller;

import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.feign.DeviceTypeService;
import com.ecoeler.feign.WebDeviceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备端口
 *
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/device")
public class DeviceController {
    @Autowired
    private WebDeviceService webDeviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    /**
     * 分页按条件查询设备列表
     * @return
     */
    @PreAuthorize("hasAuthority('DeviceList')")
    @RequestMapping("query/list")
    public Result queryDeviceList(WebDeviceDto webDeviceDto) {
        log.info("smart-home-web->RoleController->begin query device list");
        return webDeviceService.queryDeviceList(webDeviceDto);
    }
    /**
     * 分页按条件查询设备类型列表
     * @return
     */
    @PreAuthorize("hasAuthority('DeviceType')")
    @RequestMapping("query/device/type/list")
    public Result queryDeviceTypeList(WebDeviceTypeDto webDeviceTypeDto) {
        log.info("smart-home-web->RoleController->begin query device type list");
        return deviceTypeService.queryDeviceTypeList(webDeviceTypeDto);
    }

    /***
     * 查询所有设备类型
     * @return 设备类型列表
     */
    @PreAuthorize("hasAuthority('DeviceList')")
    @RequestMapping("/query/combo/box/device/type")
    public Result queryComboBoxDeviceTypeList() {
        log.info("smart-home-web->RoleController->begin query combo box device type list");
        return webDeviceService.queryAllDeviceTypeList();
    }
}

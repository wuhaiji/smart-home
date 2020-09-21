package com.ecoeler.web.controller;

import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.feign.WebDeviceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色端口
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
    /**
     * 分页按条件查询设备列表
     * @return
     */
    @RequestMapping("query/list")
    public Result queryDeviceList(WebDeviceDto webDeviceDto) {
        log.info("smart-home-web->RoleController->begin query device list");
        return webDeviceService.queryDeviceList(webDeviceDto);
    }
    /**
     * 分页按条件查询设备列表
     * @return
     */
    @RequestMapping("query/device/type/list")
    public Result queryDeviceTypeList(WebDeviceTypeDto webDeviceTypeDto) {
        log.info("smart-home-web->RoleController->begin query device type list");
        return webDeviceService.queryDeviceTypeList(webDeviceTypeDto);
    }
}
package com.ecoeler.web.controller;

import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.feign.DeviceTypeService;
import com.ecoeler.feign.WebDeviceService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备端口
 *
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/device-type")
public class DeviceTypeController {
    @Autowired
    private DeviceTypeService deviceTypeService;
    @PostMapping("/update")
    Result update(DeviceType deviceType){
        log.info("devicetype:{}",deviceType);
        ExceptionUtil.notNull(deviceType.getId(), TangCode.CODE_ID_NULL_ERROR);
        log.info("smart-home-web->DeviceTypeController->begin update device type");
        return deviceTypeService.update(deviceType);
    }

}

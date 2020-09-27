package com.ecoeler.controller;

import com.ecoeler.app.dto.v1.DeviceControlDto;
import com.ecoeler.app.service.IDeviceSwitchService;
import com.ecoeler.model.code.InviteCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujihong
 */
@RestController
public class DeviceSwitchController {

    @Autowired
    private IDeviceSwitchService iDeviceSwitchService;

    @RequestMapping("/open/all/switch")
    public Result openSwitch(DeviceControlDto deviceControlDto) {
        ExceptionUtil.notNull(deviceControlDto, InviteCode.PARAM_EMPTY_ERROR);
        iDeviceSwitchService.openSwitch(deviceControlDto);
        return Result.ok();
    }

    @RequestMapping("/close/all/switch")
    public Result closeSwitch(DeviceControlDto deviceControlDto) {
        ExceptionUtil.notNull(deviceControlDto, InviteCode.PARAM_EMPTY_ERROR);
        iDeviceSwitchService.closeSwitch(deviceControlDto);
        return Result.ok();
    }
}

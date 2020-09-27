package com.ecoeler.app.controller;

import com.ecoeler.app.dto.v1.DeviceControlDto;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.feign.DeviceSwitchService;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujihong
 */
@RestController
@RequestMapping("/smart/home")
public class DeviceSwitchController {

    @Autowired
    private DeviceSwitchService deviceSwitchService;

    /**
     * 一键开启操作
     * @author wujihong
     * @param deviceControlDto
     * @since 15:32 2020-09-25
     */
    @RequestMapping("/open/all/switch")
    public Result openSwitch(DeviceControlDto deviceControlDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(DeviceControlDto.class, deviceControlDto), WJHCode.PARAM_EMPTY_ERROR);
        return deviceSwitchService.openSwitch(deviceControlDto);
    }

    /**
     * 一键关闭操作
     * @author wujihong
     * @param deviceControlDto
     * @since 15:32 2020-09-25
     */
    @RequestMapping("/close/all/switch")
    public Result closeSwitch(DeviceControlDto deviceControlDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(DeviceControlDto.class, deviceControlDto), WJHCode.PARAM_EMPTY_ERROR);
        return deviceSwitchService.closeSwitch(deviceControlDto);
    }
}

package com.ecoeler.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.aspect.GoogleRequestSync;
import com.ecoeler.app.dto.v1.DeviceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.feign.DeviceService;
import com.ecoeler.feign.DeviceTypeService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 设备
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/device-type")
public class DeviceTypeController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @PostMapping("/primary")
    public Result primary(){
        return deviceTypeService.detailList();
    }

}

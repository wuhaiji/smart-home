package com.ecoeler.app.controller;

import com.ecoeler.feign.DeviceKeyService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备键
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/device-key")
public class DeviceKeyController {

    @Autowired
    private DeviceKeyService deviceKeyService;

    @RequestMapping("/list/device/key")
    public Result listDeviceKey(String productId){
        ExceptionUtil.notBlank(productId, TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);
        return deviceKeyService.listDeviceKey(productId);
    }


}

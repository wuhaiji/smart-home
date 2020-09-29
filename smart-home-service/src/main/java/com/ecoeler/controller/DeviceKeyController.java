package com.ecoeler.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.DeviceKey;
import com.ecoeler.app.service.IDeviceKeyService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备键
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/device-key")
public class DeviceKeyController {

    @Autowired
    private IDeviceKeyService deviceKeyService;

    @PostMapping("/list/device/key")
    public Result<List<DeviceKey>> listDeviceKey(@RequestParam String productId){
        QueryWrapper<DeviceKey> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("product_id",productId);
        return Result.ok(deviceKeyService.list(queryWrapper));
    }




}

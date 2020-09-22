package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 设备
 * @author tang
 * @since 2020/9/17
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @PostMapping("/list/room/device")
    public Result listRoomDevice(@RequestParam Long roomId){
        QueryWrapper<Device> q=new QueryWrapper<>();
        q.eq("room_id",roomId);
        return Result.ok(deviceService.list(q));
    }

    @PostMapping("/list/family/device")
    public Result listFamilyDevice(@RequestParam Long familyId){
        QueryWrapper<Device> q=new QueryWrapper<>();
        q.eq("family_id",familyId);
        return Result.ok(deviceService.list(q));
    }

    @PostMapping("/move/device")
    public Result moveDevice(@RequestParam Long deviceId,@RequestParam Long roomId){
        UpdateWrapper<Device> q=new UpdateWrapper<>();
        q.eq("device_id",deviceId).set("room_id",roomId);
        deviceService.update(q);
        return Result.ok();
    }

}

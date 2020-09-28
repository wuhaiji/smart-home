package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public Result moveDevice(@RequestParam String deviceId,@RequestParam Long roomId){
        UpdateWrapper<Device> q=new UpdateWrapper<>();
        q.eq("device_id",deviceId).set("room_id",roomId);
        deviceService.update(q);
        return Result.ok();
    }

    @PostMapping("/control")
    public Result control(@RequestBody OrderInfo orderInfo){
        deviceService.control(orderInfo);
        return Result.ok();
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id){
        deviceService.deleteDevice(id);
        return Result.ok();
    }
    @PostMapping("/save")
    public Result save( @RequestBody Device device){
        Long id=deviceService.addDevice(device);
        return Result.ok(id);
    }

    @PostMapping("/remove/device")
    public Result removeDevice(@RequestBody List<Long> roomIdList) {
        ExceptionUtil.notNull(roomIdList, WJHCode.ROOM_ID_EMPTY_ERROR);
        return Result.ok(deviceService.removeDevice(roomIdList));
    }

}

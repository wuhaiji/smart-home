package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ecoeler.app.dto.v1.DeviceDto;
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
    /**
     * 修改设备
     * @param deviceDto 只修改设备的名称
     */
    @PostMapping("/update")
    public Result update( @RequestBody DeviceDto deviceDto){
        deviceService.updateDevice(deviceDto);
        return Result.ok();
    }

    @PostMapping("/remove/device")
    public Result removeDevice(@RequestBody List<Long> roomIdList, @RequestParam Long familyId, @RequestParam Boolean removeFamilyBool) {
        ExceptionUtil.notNull(roomIdList.size() == 0, WJHCode.ROOM_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(removeFamilyBool == null, WJHCode.REMOVE_FAMILY_BOOL_EMPTY_ERROR);
        ExceptionUtil.notNull(removeFamilyBool == true && familyId == null, WJHCode.FAMILY_ID_EMPTY_ERROR);
        return Result.ok(deviceService.removeDevice(roomIdList, familyId, removeFamilyBool));
    }

}

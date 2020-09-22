package com.ecoeler.app.controller;


import com.ecoeler.feign.DeviceService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/list/room/device")
    public Result listRoomDevice(Long roomId){
        ExceptionUtil.notNull(roomId, TangCode.CODE_ROOM_ID_NULL_ERROR);
        return deviceService.listRoomDevice(roomId);
    }

    @RequestMapping("/list/family/device")
    public Result listFamilyDevice(Long familyId){
        return deviceService.listFamilyDevice(familyId);
    }

    @RequestMapping("/move/device")
    public Result moveDevice(Long deviceId, Long roomId){
        return deviceService.moveDevice(deviceId,roomId);
    }

}

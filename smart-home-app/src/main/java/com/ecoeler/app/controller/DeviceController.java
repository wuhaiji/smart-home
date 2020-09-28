package com.ecoeler.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.aspect.GoogleRequestSync;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.feign.DeviceService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        ExceptionUtil.notNull(familyId,TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return deviceService.listFamilyDevice(familyId);
    }

    @GoogleRequestSync
    @RequestMapping("/move/device")
    public Result moveDevice(String deviceId, Long roomId){
        ExceptionUtil.notBlank(deviceId,TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(roomId,TangCode.CODE_ROOM_ID_NULL_ERROR);
        return deviceService.moveDevice(deviceId,roomId);
    }

    @RequestMapping("/control")
    public Result control( String deviceId,String productId,String msg ){
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setProductId(productId);
        orderInfo.setDeviceId(deviceId);
        orderInfo.setMsg(JSONObject.parseObject(msg));
        ExceptionUtil.notBlank(orderInfo.getDeviceId(),TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(orderInfo.getProductId(),TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(orderInfo.getMsg(),TangCode.CODE_ORDER_MSG_NULL_ERROR);
        return deviceService.control(orderInfo);
    }

    @PostMapping("/delete")
    public Result delete(Long id){

        return deviceService.delete(id);
    }
    @PostMapping("/save")
    public Result save( Device device){
        ExceptionUtil.notBlank(device.getDeviceId(), TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(device.getProductId(), TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(device.getDeviceName(), TangCode.CODE_PRODUCT_NAME_EMPTY_ERROR);
        //ExceptionUtil.notNull(device.getRoomId(), TangCode.CODE_ROOM_ID_NULL_ERROR);
        ExceptionUtil.notNull(device.getFamilyId(), TangCode.CODE_FAMILY_ID_NULL_ERROR);

        ExceptionUtil.notBlank(device.getPositionName(), TangCode.CODE_POSITION_NAME_EMPTY_ERROR);
        return deviceService.save(device);
    }

    /**
     * 软删除房间下的设备（将roomId重置为0）
     * @author wujihong
     * @param roomIdList
     * @since 18:23 2020-09-27
     */
    @RequestMapping("/remove/device")
    public Result removeDevice(List<Long> roomIdList){
        ExceptionUtil.notNull(roomIdList, WJHCode.ROOM_ID_EMPTY_ERROR);
        return deviceService.removeDevice(roomIdList);
    }

}

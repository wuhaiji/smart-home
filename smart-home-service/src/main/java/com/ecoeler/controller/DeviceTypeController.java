package com.ecoeler.controller;


import com.ecoeler.app.bean.v1.DeviceTypeBean;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.service.IDeviceTypeService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 设备类型
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/device-type")
public class DeviceTypeController {

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @PostMapping("/list")
    public Result list(){
        return Result.ok(deviceTypeService.list());
    }
    @PostMapping("/detail/list")
    public Result detailList(@RequestParam String local){
        Map<String, List<DeviceType>> result =deviceTypeService.detailList(local);
        return Result.ok(result);
    }
    @PostMapping("/app/list")
    public Result appList(){
        return Result.ok(deviceTypeService.appList());
    }

    @PostMapping("/update")
    public Result update(@RequestBody DeviceType deviceType){
        deviceTypeService.updateById(deviceType);
        return Result.ok();
    }


}

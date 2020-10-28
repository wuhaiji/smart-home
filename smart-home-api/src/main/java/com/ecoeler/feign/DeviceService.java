package com.ecoeler.feign;


import com.ecoeler.app.dto.v1.DeviceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "smart-home-service", path = "/device", contextId = "device")
public interface DeviceService {

    @PostMapping("/list/room/device")
    Result listRoomDevice(@RequestParam Long roomId);

    @PostMapping("/list/family/device")
    Result listFamilyDevice(@RequestParam Long familyId);

    @PostMapping("/move/device")
    Result moveDevice(@RequestParam String deviceId, @RequestParam Long roomId);

    @PostMapping("/control")
    Result control(@RequestBody OrderInfo orderInfo);

    @PostMapping("/delete")
    Result delete(@RequestParam Long id);

    @PostMapping("/save")
    Result save(@RequestBody Device device);

    /**
     * 修改设备
     *
     * @param deviceDto 只修改设备的名称
     */
    @PostMapping("/update")
    Result update(@RequestBody DeviceDto deviceDto);

    @PostMapping("/remove/device")
    Result removeDevice(@RequestBody List<Long> roomIdList, @RequestParam Long familyId, @RequestParam Boolean removeFamilyBool);

    /**
     * 查询楼层下设备列表
     *
     * @param floorId 楼层id
     */
    @PostMapping("/floor/list")
    Result<List<Device>> getFloorDeviceList(@RequestParam Long floorId);
}

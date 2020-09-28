package com.ecoeler.feign;


import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "smart-home-service", path = "/device",contextId = "device")
public interface DeviceService {

    @PostMapping("/list/room/device")
    Result listRoomDevice(@RequestParam Long roomId);

    @PostMapping("/list/family/device")
    Result listFamilyDevice(@RequestParam Long familyId);

    @PostMapping("/move/device")
    Result moveDevice(@RequestParam String deviceId, @RequestParam Long roomId);

    @PostMapping("/control")
    Result control(@RequestBody OrderInfo orderInfo);

    @PostMapping("/remove/device")
    Result removeDevice(@RequestBody List<Long> roomIdList, @RequestParam Long familyId, @RequestParam Boolean removeFamilyBool);
}

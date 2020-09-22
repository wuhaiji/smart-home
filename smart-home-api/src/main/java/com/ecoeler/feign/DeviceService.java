package com.ecoeler.feign;


import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "smart-home-service", path = "/device",contextId = "device")
public interface DeviceService {

    @PostMapping("/list/room/device")
    Result listRoomDevice(@RequestParam Long roomId);

    @PostMapping("/list/family/device")
    Result listFamilyDevice(@RequestParam Long familyId);

    @PostMapping("/move/device")
    Result moveDevice(@RequestParam Long deviceId,@RequestParam Long roomId);

}

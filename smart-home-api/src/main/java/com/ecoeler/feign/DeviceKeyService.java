package com.ecoeler.feign;


import com.ecoeler.app.entity.DeviceKey;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "smart-home-service", path = "/device-key",contextId = "device-key")
public interface DeviceKeyService {

    @PostMapping("/list/device/key")
    Result<List<DeviceKey>> listDeviceKey(@RequestParam String productId);

}

package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.DeviceControlDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wujihong
 */
@FeignClient(value = "smart-home-service", contextId = "Switch")
public interface DeviceSwitchService {

    @PostMapping("/open/all/switch")
    Result openSwitch(@RequestBody DeviceControlDto deviceControlDto);

    @PostMapping("/close/all/switch")
    Result closeSwitch(@RequestBody DeviceControlDto deviceControlDto);
}

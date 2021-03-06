package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TangCX
 */

@FeignClient(name = "smart-home-service", contextId = "webDevice")
public interface WebDeviceService {
    /**
     * 按条件分页查询设备列表
     * @param webDeviceDto 查询条件
     * @return
     */
    @RequestMapping("/web_device/query/device/list")
     Result queryDeviceList(@RequestBody WebDeviceDto webDeviceDto);


    /***
     * 查询所有设备类型
     * @return 设备类型列表
     */
    @RequestMapping("/web_device/query/all/device/type")
    Result queryAllDeviceTypeList();

}


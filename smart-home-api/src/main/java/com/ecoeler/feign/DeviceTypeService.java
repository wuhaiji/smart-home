package com.ecoeler.feign;


import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "smart-home-service", path = "/device-type", contextId = "device-type")
public interface DeviceTypeService {
    /**
     * 设备类型一二级
     * @return
     * @param local
     */
    @PostMapping("/detail/list")
    Result detailList(@RequestParam String local);
    /**
     * 修改设备类型
     * @param deviceType
     * @return
     */
    @PostMapping("/update")
    Result update(@RequestBody DeviceType deviceType);

    /**
     * appList
     * @return
     */
    @PostMapping("/app/list")
    Result appList();
}

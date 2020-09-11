package com.ecoeler.feign;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * app voice feign
 *
 * @author whj
 * @since 2020/9/11
 */
@FeignClient(
        value = "smart-home-service",
        path = "/app-voice"
)
public interface AppVoiceFeignClient {

    /**
     * 语音请求action
     *
     * @param data      请求数据
     * @param headerMap 请求头map
     * @return
     */
    @PostMapping("/google/action")
    Result<String> action(JSONObject data, Map headerMap);

}
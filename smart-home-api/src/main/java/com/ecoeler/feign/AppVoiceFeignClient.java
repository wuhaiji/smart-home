package com.ecoeler.feign;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * app voice feign
 *
 * @author whj
 * @since 2020/9/11
 */
@FeignClient(name = "smart-home-service", path = "/app/voice", contextId = "voice")
public interface AppVoiceFeignClient {

    /**
     * 语音请求action
     *
     * @param data 请求数据
     * @return
     */
    @PostMapping("/action")
    Result<String> action(@RequestBody JSONObject data);

    /**
     * google 请求同步
     *
     * @param dto 用户信息
     * @return
     */
    @PostMapping("/request/sync")
    Result<String> requestSync(@RequestBody UserVoiceDto dto);

}

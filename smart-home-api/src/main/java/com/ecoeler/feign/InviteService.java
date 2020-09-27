package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wujihong
 */
@FeignClient(name = "smart-home-service", contextId = "inviteService", path = "/invite")
public interface InviteService {

    @RequestMapping("/send/invite")
    Result sendInvite(@RequestBody InviteRecordDto inviteRecordDto);

    @RequestMapping("/accept/invite")
    Result acceptInvite(@RequestParam Long id);

    @RequestMapping("/refuse/invite")
    Result refuseInvite(@RequestParam Long id);
}

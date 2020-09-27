package com.ecoeler.controller;

import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.service.IInviteService;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujihong
 */
@RequestMapping("/invite")
@RestController
public class InviteController {

    @Autowired
    private IInviteService iInviteService;

    @RequestMapping("/send/invite")
    public Result sendInvite(@RequestBody InviteRecordDto inviteRecordDto) {
        ExceptionUtil.notNull(inviteRecordDto, WJHCode.SEND_INVITE_SERVICE_ERROR);
        return Result.ok(iInviteService.sendInvite(inviteRecordDto));
    }

    @RequestMapping("/accept/invite")
    public Result acceptInvite(@RequestParam Long id) {
        ExceptionUtil.notNull(id, WJHCode.PARAM_EMPTY_ERROR);
        return Result.ok(iInviteService.acceptInvite(id));
    }

    @RequestMapping("/refuse/invite")
    public Result refuseInvite(@RequestParam Long id) {
        ExceptionUtil.notNull(id, WJHCode.PARAM_EMPTY_ERROR);
        return Result.ok(iInviteService.refuseInvite(id));
    }

}

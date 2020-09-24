package com.ecoeler.app.controller;

import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.feign.InviteService;
import com.ecoeler.model.code.InviteCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujihong
 */
@RequestMapping("/smart/home")
@RestController
public class OpenInviteController {

    @Autowired
    private InviteService inviteService;

    /**
     * 发送邀请函（添加要加入的家庭成员）
     * @author wujihong
     * @param inviteRecordDto
     * @since 14:21 2020-09-22
     */
    @RequestMapping("/send/invite")
    public Result sendInvite(InviteRecordDto inviteRecordDto) {
        ExceptionUtil.notNull(inviteRecordDto, InviteCode.SEND_INVITE_SERVICE_ERROR);
        return inviteService.sendInvite(inviteRecordDto);
    }

    /**
     * 接受邀请
     * @author wujihong
     * @param
     * @since 16:28 2020-09-22
     */
    @RequestMapping("/accept/invite")
    public Result acceptInvite(Long id) {
        return null;
    }

    /**
     * 拒绝邀请
     * @author wujihong
     * @param
     * @since 16:29 2020-09-22
     */
    @RequestMapping("/refuse/invite")
    public Result refuseInvite(Long id) {
        return null;
    }
}

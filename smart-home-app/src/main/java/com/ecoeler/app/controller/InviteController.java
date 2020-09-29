package com.ecoeler.app.controller;

import com.ecoeler.app.dto.v1.FloorDto;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.utils.PrincipalUtil;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.feign.InviteService;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wujihong
 */
@Controller
public class InviteController {

    @Autowired
    private InviteService inviteService;

    public final String bodyString = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"utf-8\" />\n" +
            "\t\t<title>Accept Family Invitation</title>\n" +
            "\t</head>\n" +
            "\t<body>\n" +
            "\t\t<div style=\"background-color: rgb(248,249,250);width: 100%;text-align: center;\">\n" +
            "\t\t\t<div style=\"width: 600px;display: inline-block;\">\n" +
            "\t\t\t\t<div style=\"line-height: 50px;font-size: 24px;color: rgb(31,143,235);font-style: italic;\">Yoti Smart Home</div>\n" +
            "\t\t\t\t<div style=\"padding: 24px;background-color: #fff;text-align: left;\">\n" +
            "\t\t\t\t\t<p>Dear friend,</p>\n" +
            "\t\t\t\t\t<p style=\"color: red;text-align: center;font-size: 30px;\">${message}</p>\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<div style=\"line-height: 50px;font-size: 12px;color: #95A1AC;\">\n" +
            "\t\t\t\t\tZHEJIANG YUANTAI ELECTRICAL TECHNOLOGY CO., LTD.\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</div>\n" +
            "\t</body>\n" +
            "</html>\n";

    /**
     * 发送邀请函（添加要加入的家庭成员）
     * @author wujihong
     * @param inviteRecordDto
     * @since 14:21 2020-09-22
     */
    @ResponseBody
    @RequestMapping("/smart/home/send/invite")
    public Result sendInvite(InviteRecordDto inviteRecordDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(InviteRecordDto.class, inviteRecordDto), WJHCode.PARAM_EMPTY_ERROR);
        inviteRecordDto.setInviterId(PrincipalUtil.getUserId());
        return inviteService.sendInvite(inviteRecordDto);
    }

    /**
     * 接受邀请
     * @author wujihong
     * @param
     * @since 16:28 2020-09-22
     */
    @ResponseBody
    @RequestMapping("/open/smart/home/accept/invite")
    public String acceptInvite(Long id, String inviteTime, HttpServletResponse response) {
        ExceptionUtil.notNull(id, WJHCode.INVITE_RECORD_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(inviteTime, WJHCode.INVITE_RECORD_TIME_EMPTY_ERROR);
        Result result = inviteService.acceptInvite(id, inviteTime);
        String body = bodyString;
        if (result.getData() != null) {
//            body = body.replace("${message}", JSONObject.toJSONString(result.getData()));
            body = body.replace("${message}", "ok");
        }else {
//            body = body.replace("${message}", result.getMessage());
            body = body.replace("${message}", "failed");
        }
        response.setContentType("text/html;charset=utf-8");
        return body;
    }

    /**
     * 拒绝邀请
     * @author wujihong
     * @param
     * @since 16:29 2020-09-22
     */
    @ResponseBody
    @RequestMapping("/open/smart/home/refuse/invite")
    public String refuseInvite(Long id, String inviteTime, HttpServletResponse response) {
        ExceptionUtil.notNull(id, WJHCode.INVITE_RECORD_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(inviteTime, WJHCode.INVITE_RECORD_TIME_EMPTY_ERROR);
        Result result = inviteService.refuseInvite(id, inviteTime);
        String body = bodyString;
        if (result.getData() != null) {
//            body = body.replace("${message}", JSONObject.toJSONString(result.getData()));
            body = body.replace("${message}", "ok");
        }else {
//            body = body.replace("${message}", result.getMessage());
            body = body.replace("${message}", "failed");
        }
        response.setContentType("text/html;charset=utf-8");
        return body;
    }
}

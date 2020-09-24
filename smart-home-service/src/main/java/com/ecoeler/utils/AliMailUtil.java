package com.ecoeler.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.service.impl.InviteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 阿里邮箱
 * @author tang
 * @since 2020/9/14
 */
@Slf4j
@Component
public class AliMailUtil {


    @Value("${ali.account.accessKey}")
    private String accessKey;

    @Value("${ali.account.secret}")
    private String secret;

    @Value("${invite.host.accept}")
    private String acceptInviteAddress;

    @Value("${invite.host.refuse}")
    private String refuseInviteAddress;

    public static Logger logger = LoggerFactory.getLogger(AliMailUtil.class);

    public void sendCode(String mail,String code) {
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                accessKey,
                secret
        );
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        //try {
        //DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm",  "dm.ap-southeast-1.aliyuncs.com");
        //} catch (ClientException e) {
        //e.printStackTrace();
        //}
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            //request.setAccountName("发信地址");
            request.setAccountName("admin@www.ecoeler.com");
            //request.setFromAlias("发信人昵称");
            request.setAddressType(1);
            //request.setTagName("控制台创建的标签");
            request.setReplyToAddress(true);
            //request.setToAddress("目标地址");
            request.setToAddress(mail);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress("邮箱1,邮箱2");
            //request.setSubject("邮件主题");
            request.setSubject("Yoti:Verification Code");
            //request.setHtmlBody("邮件正文");
            request.setHtmlBody(
                            "Code:" +code+ "<br>" +
                            "Verification code is valid for 5 minutes");
            log.info("send code "+code+" to mail:"+mail);
            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse response = client.getAcsResponse(request);

        } catch (ServerException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }
    }


    @Async
    public void sendInvite(InviteRecordDto inviteRecordDto){
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(
                "ap-southeast-1",
                accessKey,
                secret
        );
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName("admin@www.ecoeler.com");
            request.setAddressType(1);
            request.setReplyToAddress(true);
            request.setToAddress(inviteRecordDto.getReceiverEmail());
            request.setSubject("Yoti Smart Home:Family Inviting");

            String body="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\t<head>\n" +
                    "\t\t<meta charset=\"utf-8\" />\n" +
                    "\t\t<title>Family Invitation</title>\n" +
                    "\t</head>\n" +
                    "\t<body>\n" +
                    "\t\t<div style=\"background-color: rgb(248,249,250);width: 100%;text-align: center;\">\n" +
                    "\t\t\t<div style=\"width: 600px;display: inline-block;\">\n" +
                    "\t\t\t\t<div style=\"line-height: 50px;font-size: 24px;color: rgb(31,143,235);font-style: italic;\">Yoti Smart Home</div>\n" +
                    "\t\t\t\t<div style=\"padding: 24px;background-color: #fff;text-align: left;\">\n" +
                    "\t\t\t\t\t<p>Hi there,</p>\n" +
                    "\t\t\t\t\t<p>${name} invites you to join their ${family} family, please decide whether to join?</p>\n" +
                    "\t\t\t\t\t<p>\n" +
                    "\t\t\t\t\t\t<a href=\"${acceptInviteAddress}?id=${inviteId}\" style=\"\n" +
                    "\t\t\t\t\t\ttext-decoration: none;\n" +
                    "\t\t\t\t\t\tcolor:#fff;\n" +
                    "\t\t\t\t\t\tbackground-color:rgb(31,143,235);\n" +
                    "\t\t\t\t\t\twidth: 200px;\n" +
                    "\t\t\t\t\t\ttext-align: center;\n" +
                    "\t\t\t\t\t\tline-height: 50px;\n" +
                    "\t\t\t\t\t\tborder-radius: 8px;\n" +
                    "\t\t\t\t\t\tdisplay: inline-block;\n" +
                    "\t\t\t\t\t\tfont-size: 20px;\">I'd like to</a>\n" +
                    "\t\t\t\t\t\t<a href=\"${refuseInviteAddress}?id=${inviteId}\" style=\"\n" +
                    "\t\t\t\t\t\ttext-decoration: none;\n" +
                    "\t\t\t\t\t\tcolor:#fff;\n" +
                    "\t\t\t\t\t\tbackground-color:rgb(230,76,79);\n" +
                    "\t\t\t\t\t\twidth: 200px;\n" +
                    "\t\t\t\t\t\ttext-align: center;\n" +
                    "\t\t\t\t\t\tline-height: 50px;\n" +
                    "\t\t\t\t\t\tborder-radius: 8px;\n" +
                    "\t\t\t\t\t\tdisplay: inline-block;\n" +
                    "\t\t\t\t\t\tfont-size: 20px;\">I refuse</a>\n" +
                    "\t\t\t\t\t</p>\n" +
                    "\t\t\t\t\t<p>Yoti Team</p>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t\t<div style=\"line-height: 50px;font-size: 12px;color: #95A1AC;\">\n" +
                    "\t\t\t\t\tZHEJIANG YUANTAI ELECTRICAL TECHNOLOGY CO., LTD.\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</div>\n" +
                    "\t</body>\n" +
                    "</html>\n";
            body = body.replace("${acceptInviteAddress}", acceptInviteAddress)
                    .replace("${refuseInviteAddress}", refuseInviteAddress)
                    .replace("${name}", inviteRecordDto.getInviterName())
                    .replace("${family}", inviteRecordDto.getFamilyName())
                    .replace("${inviteId}", inviteRecordDto.getId().toString());
            request.setHtmlBody(body);
            SingleSendMailResponse response = client.getAcsResponse(request);

        } catch (ServerException e) {
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }
    }

    @Async
    public void responseInvite(InviteRecordDto inviteRecordDto){
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(
                "ap-southeast-1",
                accessKey,
                secret
        );
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName("admin@www.ecoeler.com");
            request.setAddressType(1);
            request.setReplyToAddress(true);
            request.setToAddress(inviteRecordDto.getReceiverEmail());
            request.setSubject("Yoti Smart Home:Family Inviting");

            String body="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\t<head>\n" +
                    "\t\t<meta charset=\"utf-8\" />\n" +
                    "\t\t<title>Family Invitation</title>\n" +
                    "\t</head>\n" +
                    "\t<body>\n" +
                    "\t\t<div style=\"background-color: rgb(248,249,250);width: 100%;text-align: center;\">\n" +
                    "\t\t\t<div style=\"width: 600px;display: inline-block;\">\n" +
                    "\t\t\t\t<div style=\"line-height: 50px;font-size: 24px;color: rgb(31,143,235);font-style: italic;\">Yoti Smart Home</div>\n" +
                    "\t\t\t\t<div style=\"padding: 24px;background-color: #fff;text-align: left;\">\n" +
                    "\t\t\t\t\t<p>Hi there,</p>\n" +
                    "\t\t\t\t\t<p>${name} invites you to join their ${family} family, please decide whether to join?</p>\n" +
                    "\t\t\t\t\t<p>\n" +
                    "\t\t\t\t\t\t<a href=\"${acceptInviteAddress}?id=${inviteId}\" style=\"\n" +
                    "\t\t\t\t\t\ttext-decoration: none;\n" +
                    "\t\t\t\t\t\tcolor:#fff;\n" +
                    "\t\t\t\t\t\tbackground-color:rgb(31,143,235);\n" +
                    "\t\t\t\t\t\twidth: 200px;\n" +
                    "\t\t\t\t\t\ttext-align: center;\n" +
                    "\t\t\t\t\t\tline-height: 50px;\n" +
                    "\t\t\t\t\t\tborder-radius: 8px;\n" +
                    "\t\t\t\t\t\tdisplay: inline-block;\n" +
                    "\t\t\t\t\t\tfont-size: 20px;\">I'd like to</a>\n" +
                    "\t\t\t\t\t\t<a href=\"${refuseInviteAddress}?id=${inviteId}\" style=\"\n" +
                    "\t\t\t\t\t\ttext-decoration: none;\n" +
                    "\t\t\t\t\t\tcolor:#fff;\n" +
                    "\t\t\t\t\t\tbackground-color:rgb(230,76,79);\n" +
                    "\t\t\t\t\t\twidth: 200px;\n" +
                    "\t\t\t\t\t\ttext-align: center;\n" +
                    "\t\t\t\t\t\tline-height: 50px;\n" +
                    "\t\t\t\t\t\tborder-radius: 8px;\n" +
                    "\t\t\t\t\t\tdisplay: inline-block;\n" +
                    "\t\t\t\t\t\tfont-size: 20px;\">I refuse</a>\n" +
                    "\t\t\t\t\t</p>\n" +
                    "\t\t\t\t\t<p>Yoti Team</p>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t\t<div style=\"line-height: 50px;font-size: 12px;color: #95A1AC;\">\n" +
                    "\t\t\t\t\tZHEJIANG YUANTAI ELECTRICAL TECHNOLOGY CO., LTD.\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</div>\n" +
                    "\t</body>\n" +
                    "</html>\n";
            body = body.replace("${acceptInviteAddress}", acceptInviteAddress)
                    .replace("${refuseInviteAddress}", refuseInviteAddress)
                    .replace("${name}", inviteRecordDto.getInviterName())
                    .replace("${family}", inviteRecordDto.getFamilyName())
                    .replace("${inviteId}", inviteRecordDto.getId().toString());
            request.setHtmlBody(body);
            logger.info(body);
            SingleSendMailResponse response = client.getAcsResponse(request);

        } catch (ServerException e) {
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }
    }


}

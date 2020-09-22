package com.ecoeler.model.code;

/**
 * @author wujihong
 */
public enum InviteCode implements ResultCode{

    // 参数错误
    PARAM_EMPTY_ERROR("10001", "参数不能为空！"),

    // 业务错误
    SEND_INVITE_SERVICE_ERROR("20001", "发送邀请失败！"),
    INSERT_INVITE_RECORD_SERVICE_ERROR("20002", "记录邀请信息失败！"),
    UPDATE_INVITE_RECORD_SERVICE_ERROR("20003", "修改邀请信息失败！"),

    // 其他错误
    ;

    private String code;

    private String msg;

    InviteCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

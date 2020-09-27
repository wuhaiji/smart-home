package com.ecoeler.model.code;

/**
 * @author wujihong
 */
public enum WJHCode implements ResultCode{

    // 参数错误
    PARAM_EMPTY_ERROR("10001", "参数不能为空！"),
    ROOM_ID_EMPTY_ERROR("10002", "房间id不能为空！"),
    ROOM_NAME_EMPTY_ERROR("100021", "房间名不能为空！"),
    FLOOR_ID_EMPTY_ERROR("10003", "楼层id不能为空！"),
    FLOOR_NAME_EMPTY_ERROR("100031", "楼层名不能为空！"),
    FAMILY_ID_EMPTY_ERROR("10004", "家庭id不能为空！"),

    // 业务错误
    SEND_INVITE_SERVICE_ERROR("20001", "发送邀请失败！"),
    INSERT_INVITE_RECORD_SERVICE_ERROR("20002", "记录邀请信息失败！"),
    UPDATE_INVITE_RECORD_SERVICE_ERROR("20003", "修改邀请信息失败！"),
    USER_UNREGISTERED_SERVICE_ERROR("20004", "该用户的邮箱未注册！"),

    // 其他错误
    INVITE_LOSE_EFFECT("30001", "当前邀请失效（过了有效期）！"),
    INVALID_REFUSE_INVITE("30002", "拒绝邀请无效（因为：该用户已加入到家庭）！"),
    EXIST_FAMILY_USER("30003", "被邀请用户已出现在家庭中！")
    ;

    private String code;

    private String msg;

    WJHCode(String code, String msg) {
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

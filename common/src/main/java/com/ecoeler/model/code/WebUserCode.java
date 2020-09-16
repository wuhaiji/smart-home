package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author tangCX
 * @since 2020/9/8
 */
public enum WebUserCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),
    ADD("20001","新增用户异常"),
    UPDATE("20002","修改用户信息异常"),
    DELETE("20003","删除用户信息异常"),
    SELECT("20004","分页按条件查询用户列表失败"),
    ALLOCATION("20005","分配角色异常"),
    BLANK_USER_NAME("20006","用户名不能为空"),
    BLANK_PASSWORD("20007","密码不能为空"),
    BLANK_EMAIL("20008","邮箱不能为空"),
    BLANK_PHONE_NUMBER("20009","手机号不能为空"),
    PASSWORD_NOT_IN_RANGE("20010","密码长度不在6~16之间"),
    EMAIL_NOT_MATCH("20011","邮箱格式不匹配"),
    NULL_ROLE_ID("20012","角色id不能为空"),
    NULL_ROLE("20012","角色不能为空"),
    START_TIME_AFTER_END_TIME("20013","选择的开始时间比结束时间晚")
    ;

    WebUserCode(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    /**
     * 错误码
     */
    private String resultCode;

    /**
     * 错误描述
     */
    private String resultMsg;


    @Override
    public String getCode() {
        return this.resultCode;
    }

    @Override
    public String getMsg() {
        return this.resultMsg;
    }
}

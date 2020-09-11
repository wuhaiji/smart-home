package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum WebUserCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),
    ADD("20002","新增用户异常"),
    UPDATE("20002","修改用户信息异常"),
    DELETE("20002","删除用户信息异常")
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

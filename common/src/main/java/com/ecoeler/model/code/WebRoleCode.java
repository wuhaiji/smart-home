package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum WebRoleCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),
    ADD("20002","新增角色异常"),
    UPDATE("20002","修改角色信息异常"),
    DELETE("20002","删除角色信息异常"),
    SELECT("20002","查询角色列表失败"),
    SELECT_EXCEPT_BY_ROLE_ID("20002","查询不是当前用角色列表异常")
    ;

    WebRoleCode(String resultCode, String resultMsg) {
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

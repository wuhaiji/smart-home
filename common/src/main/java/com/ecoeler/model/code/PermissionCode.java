package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum PermissionCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),
    SELECT_ALL_MENU_PERMISSION("20001","查询菜单所有权限异常"),
    SELECT_PERMISSION_BY_ROLE_ID("20001","根据角色Id查询权限异常"),
    CUSTOMIZATION("20002","定制权限异常");
    ;

    PermissionCode(String resultCode, String resultMsg) {
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

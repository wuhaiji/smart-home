package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum WebDeviceCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),

    DEVICE_MAP("20001","查询设备地图失败"),
    DEVICE_TYPE("20002","查询设备类型列表失败"),
    DEVICE_LIST("20003","查询设备列表失败"),
    ADD("20004","新增设备失败"),
    UPDATE("20005","修改设备失败"),
    DELETE("20006","删除设备失败"),
    DEVICE_TYPE_DATA("20006","查询设备参数失败")
    ;

    WebDeviceCode(String resultCode, String resultMsg) {
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

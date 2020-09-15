package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum WebCustomerCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),

    CUSTOMER_FAMILY("20001","按条件查询家庭列表失败"),
    CUSTOMER_FAMILY_MEMBER("20002","查询家庭成员失败"),
    CUSTOMER_FAMILY_ROOM("20003","查询家庭房间失败"),
    CUSTOMER_FAMILY_DEVICE("20004","查询家庭设备失败"),
    ;

    WebCustomerCode(String resultCode, String resultMsg) {
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

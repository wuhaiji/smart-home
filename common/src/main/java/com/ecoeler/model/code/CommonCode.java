package com.ecoeler.model.code;

/**
 * 常用错误信息枚举
 *
 * @author whj
 */
public enum CommonCode implements ResultCode {

    //
    SUCCESS("10000", "成功!"),
    NETWORK_ANOMALY("10004", "网络异常!"),
    LOGOUT_FAILED("10007","登出失败"),
    SERVER_ERROR("99999", "抱歉，系统繁忙，请稍后重试！");


    CommonCode(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;


    @Override
    public String getCode() {
        return this.resultCode;
    }

    @Override
    public String getMsg() {
        return this.resultMsg;
    }
}

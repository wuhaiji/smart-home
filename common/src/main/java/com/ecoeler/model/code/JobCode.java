package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public enum JobCode implements ResultCode {
    /**
     *
     */
    SUCCESS("20000", "成功!"),

    ;

    JobCode(String resultCode, String resultMsg) {
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

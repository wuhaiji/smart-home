package com.ecoeler.model.code;


public enum AppVoiceCode implements ResultCode {

    ACTION_EXECUTION_ERROR("30000", " error occurred in action execute"),

    ACTION_SELECT_DEVICE_LIST_ERROR("30001", " error occurred in query user device list "),

    ACTION_PARAMS_ERROR("30002", " userId can not be null "),
    ;


    AppVoiceCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述
     */
    private final String msg;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}

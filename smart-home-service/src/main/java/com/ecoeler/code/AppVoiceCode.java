package com.ecoeler.code;


import com.ecoeler.model.code.ResultCode;

public enum AppVoiceCode implements ResultCode {

    ACTION_EXECUTION_EXCEPTION("30000", "action执行异常"),

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

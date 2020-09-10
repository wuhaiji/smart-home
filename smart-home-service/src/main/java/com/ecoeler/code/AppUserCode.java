package com.ecoeler.code;


import com.ecoeler.model.code.ResultCode;

public enum AppUserCode implements ResultCode {

    /** 登录失败 **/
    CODE_LOGIN_ERROR("20000", "登录失败，账号或密码错误！"),

    /** 验证码错误 **/
    CODE_CAPTCHA_ERROR("20001","图片验证码错误！");


    AppUserCode(String code, String msg) {
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

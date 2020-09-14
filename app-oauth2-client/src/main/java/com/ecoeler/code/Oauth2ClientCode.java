package com.ecoeler.code;

import com.ecoeler.model.code.ResultCode;


/**
 * OAUTH2错误码
 * @author tang
 * @since 2020/9/14
 */
public enum  Oauth2ClientCode implements ResultCode {

    /** 登录失败 **/
    CODE_BAD_CREDENTIALS("40000", "登录失败，账号或密码错误");

    Oauth2ClientCode(String code,String msg){

        this.code = code;

        this.msg = msg;

    }

    private final String code;

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

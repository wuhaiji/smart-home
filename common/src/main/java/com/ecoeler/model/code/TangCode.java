package com.ecoeler.model.code;


/**
 * 错误码
 * @author tang
 * @since 2020/9/14
 */
public enum  TangCode implements ResultCode {

    //参数错误
    CODE_EMAIL_EMPTY_ERROR("40001","邮箱不能为空!"),
    CODE_PASSWORD_EMPTY_ERROR("40002","密码不能为空!"),
    CODE_CODE_EMPTY_ERROR("40003","验证码不能为空!"),
    CODE_USERNAME_EMPTY_ERROR("40004","用户名不能为空!"),

    //错误码
    CODE_LOGIN_ERROR("20000", "登录失败，账号或密码错误!"),
    CODE_CAPTCHA_ERROR("20001","图片验证码错误!"),
    CODE_LIMIT_EMAIL_ERROR("20002","两分钟内勿重复请求邮箱验证码!"),
    CODE_EMAIL_CODE_ERROR("20003","邮箱验证码错误!"),
    CODE_USER_EXIST("20004","用户已存在!");
    ;



    TangCode(String code, String msg) {
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

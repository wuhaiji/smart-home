package com.ecoeler.app.service;

/**
 * 邮箱验证码服务
 * @author tang
 * @since 2020/9/14
 */
public interface IEmailCodeService {

    /**
     * 过滤
     * @param ip
     */
    void filter(String ip);

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    void sendCode(String email);

    /**
     * 验证验证码
     * @param email
     * @param code
     * @return
     */
    boolean verify(String email,String code);

}

package com.ecoeler.model.response;

import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.ResultCode;

/**
 * <p>
 * 通用请求返回对象
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public class Result<T> {


    /**
     * 响应状态
     */
    protected String code;
    /**
     * 消息
     */
    protected String message;

    /**
     * 数据
     */
    protected T data;

    public Result() {
    }

    public Result(String message) {
        this.message = message;
    }

    public boolean success(){
        return CommonCode.SUCCESS.getCode().equals(code);
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功
     */
    public static Result<String> ok() {
        return new Result<>(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
    }


    /**
     * 返回成功
     */
    public static <T> Result<T> ok(T data) {
        return new Result<T>(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg(), data);
    }

    /**
     * 失败
     */
    public static Result<String> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg());
    }

    /**
     * 失败
     */
    public static Result<String> error(String code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 失败
     */
    public static Result<String> error(String message) {
        return new Result<>(message);
    }

    @Override
    public String toString() {
        return "{" + "\"data\":" +
                data +
                ",\"code\":\"" +
                code + '\"' +
                ",\"message\":\"" +
                message + '\"' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}

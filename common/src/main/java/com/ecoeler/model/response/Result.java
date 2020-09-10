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
public class Result {


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
    protected Object data;

    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功
     */
    public static Result success() {
        return new Result(CommonCode.SUCCESS.getResultCode(), CommonCode.SUCCESS.getResultMsg());
    }

    /**
     * 返回成功
     */
    public static Result success(Object data) {
        return new Result(CommonCode.SUCCESS.getResultCode(), CommonCode.SUCCESS.getResultMsg(), data);
    }

    /**
     * 失败
     */
    public static Result error(ResultCode resultCode) {
        return new Result(resultCode.getResultCode(), resultCode.getResultMsg());
    }

    /**
     * 失败
     */
    public static Result error(String code, String message) {
        return new Result(code, message);
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

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}

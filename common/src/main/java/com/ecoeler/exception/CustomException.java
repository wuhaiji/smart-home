package com.ecoeler.exception;


import com.ecoeler.model.code.ResultCode;
import lombok.Data;

/**
 * @author whj
 * @since  2020-02-09 17:29
 **/
@Data
public class CustomException extends RuntimeException {

    /**
     * 错误码
     */
    protected String code;
    /**
     * 错误信息
     */
    protected String msg;

    public CustomException() {
        super();
    }

    public CustomException(ResultCode errorInfoInterface) {
        super(errorInfoInterface.getCode());
        this.code = errorInfoInterface.getCode();
        this.msg = errorInfoInterface.getMsg();
    }

    public CustomException(ResultCode code, Throwable cause) {
        super(code.getCode(), cause);
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CustomException(String code, String msg) {
        super(code);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(String code, String msg, Throwable cause) {
        super(code, cause);
        this.code = code;
        this.msg = msg;
    }



    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

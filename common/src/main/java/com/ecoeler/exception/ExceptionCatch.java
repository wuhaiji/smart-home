package com.ecoeler.exception;

import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.ResultCode;
import com.ecoeler.model.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


/**
 * <p>
 * 统一异常捕获类
 * </p>
 *
 * @author whj
 * @since 2020-09-08
 **/
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger log = LoggerFactory.getLogger(ExceptionCatch.class);
    /**
     * 已知异常类型map
     */
    private static final HashMap<Class<? extends Throwable>, ResultCode> exceptionMap;

    static {
        exceptionMap = new HashMap<>();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result customException(ServiceException e) {
        log.error("catch exception:{}", e.getMsg());
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e) {
        log.error("catch exception:{}", e.getMessage());
        e.printStackTrace();

        //判断异常是否有对应resultCode
        ResultCode resultCode = exceptionMap.get(e.getClass());

        if (resultCode != null) return Result.error(resultCode);

        return Result.error(CommonCode.SERVER_ERROR);
    }
}

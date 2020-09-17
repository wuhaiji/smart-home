package com.ecoeler.app.config;

import com.ecoeler.exception.ExceptionCatch;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>
 * 统一异常捕获类
 * </p>
 *
 * @author whj
 * @since 2020-09-08
 **/
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result accessDeniedException(AccessDeniedException e) {
        return Result.error(TangCode.CODE_NO_AUTH_ERROR);
    }

    private static final Logger log = LoggerFactory.getLogger(ExceptionCatch.class);

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
        return Result.error(CommonCode.SERVER_ERROR);
    }

}

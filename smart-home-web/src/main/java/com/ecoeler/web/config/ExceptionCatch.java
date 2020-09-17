package com.ecoeler.web.config;

import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.ResultCode;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result accessDeniedException(AccessDeniedException e) {
        return Result.error(TangCode.CODE_NO_AUTH_ERROR);
    }

}

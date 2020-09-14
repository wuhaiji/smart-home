package com.ecoeler.util;

import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.ResultCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 异常工具
 * @author tang
 * @since 2020/9/14
 */
public class ExceptionUtil {

    public  static void  notNull(Object obj, ResultCode code){
        if(obj==null){
            throw new ServiceException(code.getCode(),code.getMsg());
        }
    }

    public static void notBlank(String obj,ResultCode code){
        if(StringUtils.isBlank(obj)){
            throw new ServiceException(code.getCode(),code.getMsg());
        }
    }


}

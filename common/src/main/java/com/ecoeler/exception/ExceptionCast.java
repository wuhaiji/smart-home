package com.ecoeler.exception;


import com.ecoeler.model.code.ResultCode;

/**
 * @author whj
 * @since 2020-09-08
 **/
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}

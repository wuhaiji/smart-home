package com.ecoeler.exception;


import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.ResultCode;
import com.ecoeler.model.response.Result;

/**
 * @author whj
 * @since 2020-09-08
 **/
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }

    /**
     * 处理feign http请求异常
     *
     * @param result
     */
    public static void feignCast(Result result) {
        //网络异常
        if (result == null) {
            throw new CustomException(CommonCode.NETWORK_ANOMALY);
        }
        //业务异常
        if (!CommonCode.SUCCESS.getCode().equals(result.getCode())) {
            throw new CustomException(result.getCode(), result.getMessage());
        }

    }

}

package com.ecoeler.model.code;

/**
 * <p>
 * 10000-- 通用错误代码
 * </p>
 *
 * @author whj
 * @since 2020-09-08
 */
public interface ResultCode {

    /**
     * 错误码
     */
    String getResultCode();

    /**
     * 错误描述
     */
    String getResultMsg();
}

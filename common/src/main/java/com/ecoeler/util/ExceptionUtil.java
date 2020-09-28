package com.ecoeler.util;

import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.ResultCode;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 异常工具
 *
 * @author tang
 * @since 2020/9/14
 */
public class ExceptionUtil {

    public static void notNull(Object obj, ResultCode code) {
        if (obj == null) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    public static <T> void notEqual(T obj, T target, ResultCode code) {
        if (obj == null || obj.equals(target)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    public static void notBlank(String obj, ResultCode code) {
        if (StringUtils.isBlank(obj)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    /**
     * 
     * @author wujihong
     * @param condition	
     * @param code	
     * @since 16:41 2020-09-27
     */
    public static void notNull(Boolean condition, ResultCode code) {
        // 防止condition为空值
        if (Optional.ofNullable(condition).orElse(true)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    /**
     * 验证字符串格式是否匹配
     *
     * @param obj   目标字符串
     * @param code  异常
     */
    public static void notEmail(String obj,  ResultCode code) {
        String regex="^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!obj.matches(regex)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    /**
     * 验证字符串是否在一定长度范围
     *
     * @param obj  目标字符串
     * @param min  最短长度
     * @param max  最长长度
     * @param code 异常
     */
    public static void notInRange(String obj, Integer min, Integer max, ResultCode code) {
        if (obj.length() < min || obj.length() > max) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    /**
     * 验证字符串格式是否匹配
     *
     * @param obj   目标字符串
     * @param regex 正则表达式
     * @param code  异常
     */
    public static void notMatch(String obj, String regex, ResultCode code) {
        if (!obj.matches(regex)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }

    /**
     * 验证查询时间段
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param code  异常
     */
    public static void startIsAfterEnd(LocalDateTime start, LocalDateTime end, ResultCode code) {
        if (start.isAfter(end)) {
            throw new ServiceException(code.getCode(), code.getMsg());
        }
    }


}

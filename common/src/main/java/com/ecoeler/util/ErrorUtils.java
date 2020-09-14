package com.ecoeler.util;

import com.ecoeler.exception.ServiceException;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/8
 */
public class ErrorUtils {
    public ErrorUtils() {
    }

    public static void isStringEmpty(String o, String message) throws ServiceException {
        if (StrIsEmpty(o)) {
            throw new ServiceException("S_STR_EMPTY", message + "不能为空！");
        }
    }

    private static boolean StrIsEmpty(String o) {
        return o == null || o.isEmpty() || o.trim().isEmpty();
    }

    public static void isNullable(Object o, String message) throws ServiceException {
        if (o == null) {
            throw new ServiceException("S_OBJ_EMPTY", message + "不能为空！");
        }
    }

    public static void isExist(Object o, String message) throws ServiceException {
        if (o != null) {
            throw new ServiceException("S_OBJ_EXIST", message + "已存在！");
        }
    }

    public static void isStringLengthMax(String val, int max, String message) throws ServiceException {
        if (val.length() > max) {
            throw new ServiceException("S_STR_LENGTH", message + "最多" + max + "个字符！");
        }
    }

    public static void isStringLengthMin(String val, int min, String message) throws ServiceException {
        if (val.length() < min) {
            throw new ServiceException("S_STR_LENGTH", message + "至少" + min + "个字符！");
        }
    }
    //大于等于
    public static void isNumberValueGe(Number val, Number max, String message) throws ServiceException {
        if (val.doubleValue() >= max.doubleValue()) {
            throw new ServiceException("S_NUM_LENGTH", message + "不能大于" + max);
        }
    }
    //大于
    public static void isNumberValueGt(Number val, Number max, String message) throws ServiceException {
        if (val.doubleValue() > max.doubleValue()) {
            throw new ServiceException("S_NUM_LENGTH", message + "不能大于或者等于" + max);
        }
    }
    //小于等于
    public static void isNumberValueLe(Number val, Number min, String message) throws ServiceException {
        if(val==null){
            throw new ServiceException("S_NUM_EMPTY", message + "不能为空");
        }
        if (val.doubleValue() <= min.doubleValue()) {
            throw new ServiceException("S_NUM_LENGTH", message + "不能小于或者等于" + min);
        }
    }
    //小于
    public static void isNumberValueLt(Number val, Number min, String message) throws ServiceException {
        if(val==null){
            throw new ServiceException("S_NUM_EMPTY", message + "不能为空");
        }
        if (val.doubleValue() < min.doubleValue()) {
            throw new ServiceException("S_NUM_LENGTH", message + "不能小于" + min);
        }
    }

    public static void isNullableByCustom(Object o, String message) throws ServiceException {
        if (o == null) {
            throw new ServiceException("S_OBJ_EMPTY", message);
        }
    }

    public static void isNumberValueMinByCustom(Number val, Number min, String message) throws ServiceException {
        if (val.doubleValue() < min.doubleValue()) {
            throw new ServiceException("S_NUM_SIZE", message);
        }
    }

    public static void isFalse(Boolean b, String message) throws ServiceException {
        if (!b) {
            throw new ServiceException("S_FALSE", message);
        }
    }

    public static void isTrue(Boolean b, String message) throws ServiceException {
        if (b) {
            throw new ServiceException("S_TRUE", message);
        }
    }

    public static void isExistByCustom(Object o, String message) throws ServiceException {
        if (o != null) {
            throw new ServiceException("S_OBJ_EXIST", message);
        }
    }

    public static void byCustom(String message) throws ServiceException {
        throw new ServiceException("S_CUSTOM", message);
    }

    public static void message(String message) throws ServiceException {
        throw new ServiceException("S_NORMAL_MESSAGE", message);
    }

    public static void isCollectionEmpty(Collection c, String message) throws ServiceException {
        if (CollectionUtils.isEmpty(c)) {
            throw new ServiceException("S_COLL_EMPTY", message);
        }
    }

    public static void isCountNumEqual(Integer dest, Integer src, String message) throws ServiceException {
        if (!dest.equals(src)) {
            throw new ServiceException("S_COLL_EMPTY", message);
        }
    }

    public static void isStringEmptyByCustom(String o, String message) throws ServiceException {
        if (StrIsEmpty(o)) {
            throw new ServiceException("S_STR_EMPTY", message + "！");
        }
    }

   //>=min && <=max
    public static void isNumberValueIn(Number val, Number min,Number max, String message) {
        if(val==null){
            throw new ServiceException("S_NUM_EMPTY", message + "不能为空");
        }
        if (val.doubleValue() <= min.doubleValue()||val.doubleValue() >= max.doubleValue()) {
            throw new ServiceException("S_NUM_LENGTH", message + "在" + min+"~"+max+"之间");
        }
    }


    public static void isStringMatch(String val, String regex, String message) {
        if (!val.matches(regex)){
            throw new ServiceException("S_STR_MATCH", message + "格式不匹配");
        }
    }

    public static void isTimeBefore(LocalDateTime startTime, LocalDateTime endTime, String message) {
        if (startTime.isAfter(endTime)){
            throw new ServiceException("S_TIME_AFTER", message + "开始时间不能比结束时间晚");
        }
    }
}

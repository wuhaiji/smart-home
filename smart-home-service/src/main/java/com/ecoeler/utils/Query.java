package com.ecoeler.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 *  QueryWrapper工具类
 * </p>
 *
 * @author whj
 * @since 2020/9/23
 */
public class Query {

    public static <T> QueryWrapper<T> of(Class<T> tClass) {
        return new QueryWrapper<>();
    }
}

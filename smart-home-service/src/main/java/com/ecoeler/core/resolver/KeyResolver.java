package com.ecoeler.core.resolver;

/**
 * 键分解器
 * @author tang
 * @since 2020/7/20
 */
public interface KeyResolver<T> {

    /**
     * 分解
     * @param msg
     * @return
     */
    ResolveResult resolve(T msg);

}

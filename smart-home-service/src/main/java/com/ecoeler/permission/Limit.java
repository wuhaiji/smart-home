package com.ecoeler.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 权限控制
 *
 * @author tang
 * @since 2020/8/11
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Limit {

    boolean needLogin() default true;

    //权限
    String[] hasPerm() default {};

    String[] hasRole() default {};

}

package com.ecoeler.common;

/**
 * @author wujihong
 */
public class NullContentJudge {

    /**
     * 判断对象的内容是否为空
     * @author wujihong
     * @param t1,t2
     * @since 16:29 2020-09-27
     */
    public static <T> Boolean isNullContent(Class<T> t1, T t2) {
        T init = null;
        try {
            init = t1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return init.equals(t2);
    }
}

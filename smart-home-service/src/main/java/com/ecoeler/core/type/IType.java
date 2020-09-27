package com.ecoeler.core.type;

/**
 * 类型接口
 * @author tang
 * @since 2020/7/20
 */
public interface IType {

    String TYPE_ENUM="Enum";

    String TYPE_INTEGER="Integer";

    String TYPE_BOOLEAN="Boolean";

    /**
     * 输入时匹配判断 并 改变值
     * @param value
     * @return
     */
    Object checkout(Object value);

    /**
     * 输出时匹配判断 并 改变值
     * @param value
     * @return
     */
    Object checkin(Object value);

}

package com.ecoeler.action.alexa;

import lombok.Data;

/**
 * @author whj
 * @createTime 2020-03-03 14:29
 * @description 用于StateReport响应设备状态的模型
 **/
@Data
public class Property {
    /**
     *属性对应的命名空间
     */
    private String namespace;
    /**
     *属性对应的名称
     */
    private String name;
    /**
     * 属性的当前值。
     */
    private Object value;
    /**
     * 记录属性值的时间。时间指定为ISO 8601格式的字符串，即YYYY-MM-DDThh：mm：ssZ。
     */
    private String timeOfSample;
    /**
     * 自上次确认属性值以来经过的毫秒数
     */
    private Long uncertaintyInMilliseconds;

}

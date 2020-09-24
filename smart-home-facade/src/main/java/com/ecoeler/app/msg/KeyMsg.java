package com.ecoeler.app.msg;


import lombok.Data;

/**
 * 键上传的消息
 * @author tang
 * @since 2020/9/15
 */
@Data
public class KeyMsg {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 键
     */
    private String dataKey;

    /**
     * 值
     */
    private Object dataValue;

    /**
     * 序列号
     */
    private Long seq;

}

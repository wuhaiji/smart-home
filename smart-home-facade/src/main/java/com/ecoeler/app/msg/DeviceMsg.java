package com.ecoeler.app.msg;

import lombok.Data;

/**
 * 设备上传的键
 * @author tang
 * @since 2020/7/20
 */
@Data
public class DeviceMsg {

    /**
     * 设备ID
     */
    private String devId;

    /**
     * 产品型号
     */
    private String productId;

    /**
     * 序号
     */
    private Long seq;

    /**
     * 行为
     */
    private String act;

    /**
     * 状态
     */
    private Object status;

}

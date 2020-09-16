package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author tangcx
 */
@Data
public class WebDeviceDataBean {
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
    private String dataValue;

    /**
     * 键的名称
     */
    private String keyName;


}

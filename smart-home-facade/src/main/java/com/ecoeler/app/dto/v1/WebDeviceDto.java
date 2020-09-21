package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class WebDeviceDto extends BasePageQueryTimeDto {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private String deviceTypeName;
    /**
     * 在线离线 ，1在线 ，0离线
     */
    private Integer netState;



}

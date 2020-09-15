package com.ecoeler.app.dto.v1;

import lombok.Data;


@Data
public class WebDeviceDto extends QueryTimeDto {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     *  设备名称
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
    /**
     * 查询时间段类型 0-online_time 1-offline_time 2-create_time 3-update_time
     */
    private Integer timeType;


}

package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebCustomerFamilyDeviceBean {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备类型
     */
    private String deviceTypeName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备上线日期
     */
    private LocalDateTime onlineTime;
    /**
     * 设备下线日期
     */
    private LocalDateTime offlineTime;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 在线离线 ，1在线 ，0离线
     */
    private Integer netState;

    /**
     * 设备位置
     */
    private String location;


}

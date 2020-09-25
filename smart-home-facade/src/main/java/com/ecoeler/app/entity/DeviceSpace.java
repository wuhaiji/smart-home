package com.ecoeler.app.entity;

import lombok.Data;

/**
 * 设备空间
 * @author tang
 * @since 2020/9/25
 */
@Data
public class DeviceSpace {

    private Long familyId;

    private Long roomId;

    private String roomName;

    private Long floorId;

    private String floorName;

}

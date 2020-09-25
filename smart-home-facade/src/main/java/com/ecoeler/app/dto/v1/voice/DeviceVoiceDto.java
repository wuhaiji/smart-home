package com.ecoeler.app.dto.v1.voice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceVoiceDto {

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备产品id
     */
    private String productId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备名称
     */
    private String deviceTypeName;
    /**
     * 在线离线
     */
    private Long netState;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 家庭id
     */
    private Long familyId;

    /**
     * 是否为网关设备：0 否，1 是
     */
    private Integer gatewayLike;

    public static final Integer GATEWAY_LIKE_IS_NOT = 0;

    public static final Integer GATEWAY_LIKE_IS_YES = 1;


    /**
     * 家庭ids
     */
    private List<Long> familyIds;


    /**
     * deviceIds
     */
    List<String> deviceIds;


}

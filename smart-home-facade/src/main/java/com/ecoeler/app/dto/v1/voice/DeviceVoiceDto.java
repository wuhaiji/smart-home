package com.ecoeler.app.dto.v1.voice;

import lombok.Data;
import lombok.NonNull;
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

    List<String> deviceIds;


}

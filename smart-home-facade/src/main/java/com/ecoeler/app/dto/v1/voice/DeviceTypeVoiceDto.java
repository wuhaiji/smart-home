package com.ecoeler.app.dto.v1.voice;

import lombok.Data;
import lombok.experimental.Accessors;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceTypeVoiceDto {

    /**
     * alexaDisplayName
     */
    private String alexaDisplayName;

    /**
     * googleTypeName
     */

    private String googleTypeName;

    /**
     * googleTraitName
     */
    private String googleTraitName;

    /**
     * 设备产品id
     */
    private String productId;


}

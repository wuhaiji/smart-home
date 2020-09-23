package com.ecoeler.app.dto.v1.voice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceDataVoiceDto {

    private Long id;

    private String deviceId;

    private String dataKey;

    private String seq;

    List<String> deviceIds;

}

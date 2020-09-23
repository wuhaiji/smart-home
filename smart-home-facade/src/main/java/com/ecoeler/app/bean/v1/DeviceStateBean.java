package com.ecoeler.app.bean.v1;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceStateBean {

    private String deviceId;

    private String dataKey;

    private String googleStateName;

    private String alexaStateName;

    private String alexaInterface;

    private String value;

    private LocalDateTime createTime;


}

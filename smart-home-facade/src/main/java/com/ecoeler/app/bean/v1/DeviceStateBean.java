package com.ecoeler.app.bean.v1;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class DeviceStateBean {

    private Long deviceId;

    private String googleStateName;

    private String alexaStateName;

    private String alexaNamespace;

    private String value;



}

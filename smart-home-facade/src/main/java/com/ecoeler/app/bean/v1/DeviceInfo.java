package com.ecoeler.app.bean.v1;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceInfo {

    private String deviceId;

    private Boolean online;

    private List<DeviceStateBean> deviceStateBeans;
}

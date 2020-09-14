package com.ecoeler.app.bean.v1;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class DeviceInfo {

    private Boolean online;

    private List<DeviceStateBean> deviceStateBeans;
}

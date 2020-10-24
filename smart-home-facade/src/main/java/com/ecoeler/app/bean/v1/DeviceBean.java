package com.ecoeler.app.bean.v1;

import com.ecoeler.app.entity.Device;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class DeviceBean extends Device {
    private String model;

}

package com.ecoeler.app.dto.v1;

import com.ecoeler.app.entity.DeviceSwitch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/25
 */
@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceSwitchVoiceDto extends DeviceSwitch {
    List<Long> deviceKeyIds;
}

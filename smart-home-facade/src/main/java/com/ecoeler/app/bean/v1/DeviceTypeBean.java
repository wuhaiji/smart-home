package com.ecoeler.app.bean.v1;

import com.ecoeler.app.entity.DeviceType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author Tangcx
 * @since 2020-10-16
 */
@Data
public class DeviceTypeBean {
    private List<DeviceType> primaryTypeList;
    private Map<String, List<DeviceType>> map;

}

package com.ecoeler.observer;

import com.ecoeler.app.dto.v1.FamilyDto;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.Family;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class DeviceFamilyEventObserver implements FamilyEventObserver{

    /**
     * 当删除家庭时，设备需要做的删除操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserDeleteFamily(UserFamilyDto userFamilyDto) {}

    /**
     * 当用户离开家庭时，设备需要做的操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserLeaveFamily(UserFamilyDto userFamilyDto) {}
}

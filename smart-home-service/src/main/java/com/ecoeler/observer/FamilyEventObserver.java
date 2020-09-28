package com.ecoeler.observer;

import com.ecoeler.app.dto.v1.UserFamilyDto;

/**
 * @author wujihong
 */
public interface FamilyEventObserver {

    /**
     * 当用户删除家庭时
     * @author wujihong
     * @param userFamilyDto
     * @since 16:31 2020-09-28
     */
    void whenUserDeleteFamily(UserFamilyDto userFamilyDto);

    /**
     * 当用户离开家庭时
     * @author wujihong
     * @param
     * @since 17:18 2020-09-28
     */
    void whenUserLeaveFamily(UserFamilyDto userFamilyDto);

}

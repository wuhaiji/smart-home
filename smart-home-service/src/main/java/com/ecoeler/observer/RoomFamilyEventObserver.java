package com.ecoeler.observer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.FamilyDto;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.service.IRoomService;
import com.ecoeler.constant.FamilyTypeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class RoomFamilyEventObserver implements FamilyEventObserver {

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private IRoomService roomService;

    /**
     * 当删除家庭时，房间需要做的删除操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserDeleteFamily(UserFamilyDto userFamilyDto) {
        if (userFamilyDto.getFamilyType() == null) {
            // 根据家庭id，查询家庭类型（0别墅，1住宅）
            QueryWrapper<Family> familyQueryWrapper = new QueryWrapper<>();
            familyQueryWrapper.eq("id", userFamilyDto.getFamilyId());
            Family family = familyMapper.selectOne(familyQueryWrapper);
            userFamilyDto.setFamilyType(family.getFamilyType());
        }
        if (userFamilyDto.getFamilyType() == FamilyTypeConst.HOUSE) {
            roomService.removeRoom(new RoomDto().setFamilyId(userFamilyDto.getFamilyId()).setRemoveFamilyBool(true));
        }
    }

    /**
     * 当用户离开家庭时，房间需要做的操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserLeaveFamily(UserFamilyDto userFamilyDto) {}
}

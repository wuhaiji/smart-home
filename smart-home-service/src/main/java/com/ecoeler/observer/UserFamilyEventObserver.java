package com.ecoeler.observer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.constant.FamilyRoleConst;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.WJHCode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class UserFamilyEventObserver implements FamilyEventObserver{

    @Autowired
    private UserFamilyMapper userFamilyMapper;

    /**
     * 当删除家庭时，用户家庭关系表需要做的删除操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserDeleteFamily(UserFamilyDto userFamilyDto) {
        QueryWrapper<UserFamily> userFamilyQueryWrapper = new QueryWrapper<>();
        userFamilyQueryWrapper.eq("family_id", userFamilyDto.getFamilyId());
        // 删除用户-家庭关系
        userFamilyMapper.delete(userFamilyQueryWrapper);
    }

    /**
     * 当用户离开家庭时，用户家庭关系表需要做的操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserLeaveFamily(UserFamilyDto userFamilyDto) {
        QueryWrapper<UserFamily> userFamilyQueryWrapper = new QueryWrapper<>();
        UpdateWrapper<UserFamily> userFamilyUpdateWrapper = new UpdateWrapper<>();

        userFamilyQueryWrapper.eq("family_id", userFamilyDto.getFamilyId());
        userFamilyQueryWrapper.eq("app_user_id", userFamilyDto.getAppUserId());
        userFamilyUpdateWrapper.eq("family_id", userFamilyDto.getFamilyId());
        userFamilyUpdateWrapper.eq("app_user_id", userFamilyDto.getNewAppUserOwnerId());

        // 判断该用户是否为家庭拥有者？(是)
        if (userFamilyMapper.selectOne(userFamilyQueryWrapper).getRole() == FamilyRoleConst.OWNER) {
            if (userFamilyDto.getNewAppUserOwnerId() == null) {
                throw new ServiceException(WJHCode.NEW_APP_USER_ID_EMPTY_ERROR);
            }
            // 指派新的用户
            if (userFamilyMapper.selectOne(userFamilyUpdateWrapper) == null) {
                throw new ServiceException(WJHCode.APPOINT_USER_NOT_IN_FAMILY);
            }
            userFamilyMapper.update(new UserFamily().setRole(0), userFamilyUpdateWrapper);
        }
        // 删除app用户
        userFamilyMapper.delete(userFamilyQueryWrapper);
    }


}

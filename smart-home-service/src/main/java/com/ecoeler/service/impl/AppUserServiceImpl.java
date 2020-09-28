package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.app.service.IFamilyService;
import com.ecoeler.constant.FamilyRoleConst;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.utils.WebStatisticsUtil;
import com.ecoeler.model.code.WJHCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebStatisticsUtil webStatisticsUtil;

    @Autowired
    private IFamilyService familyService;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private UserFamilyMapper userFamilyMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createUser(AppUser user) {
        //判断用户是否存在
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new ServiceException(TangCode.CODE_USER_EXIST);
        }

        //新增用户
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        baseMapper.insert(user);
        //修改统计表中的数据
        webStatisticsUtil.updateStatistics(AppUser.class);

        return user.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean leaveFamily(UserFamilyDto userFamilyDto) {
        return true;
    }

    @Override
    public Boolean dissolveFamily(UserFamilyDto userFamilyDto) {
        boolean result = false;
        QueryWrapper<UserFamily> userFamilyQueryWrapper = new QueryWrapper<>();
        userFamilyQueryWrapper.eq("family_id", userFamilyDto.getFamilyId());
        userFamilyQueryWrapper.eq("app_user_id", userFamilyDto.getAppUserId());

        // 判断该用户是否拥有删除权限？(家庭拥有者才有删除的权限)
        if (userFamilyMapper.selectOne(userFamilyQueryWrapper).getRole() != FamilyRoleConst.OWNER) {
            throw new ServiceException(WJHCode.NO_PERMISSION_ERROR);
        }
        // 删除家庭
        if (familyService.removeFamily(userFamilyDto.getFamilyId())) {
            result = true;
        }
        return result;
    }
}

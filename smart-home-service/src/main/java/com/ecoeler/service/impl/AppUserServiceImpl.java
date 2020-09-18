package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.exception.ServiceException;
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

        return user.getId();
    }
}

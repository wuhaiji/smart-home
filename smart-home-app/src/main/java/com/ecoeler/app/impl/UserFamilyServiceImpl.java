package com.ecoeler.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.app.service.IUserFamilyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class UserFamilyServiceImpl extends ServiceImpl<UserFamilyMapper, UserFamily> implements IUserFamilyService {

}

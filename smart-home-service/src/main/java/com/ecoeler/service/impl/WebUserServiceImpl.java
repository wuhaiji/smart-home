package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.entity.WebUser;
import com.ecoeler.mapper.WebUserMapper;
import com.ecoeler.service.IWebUserService;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebUserServiceImpl extends ServiceImpl<WebUserMapper, WebUser> implements IWebUserService {


}

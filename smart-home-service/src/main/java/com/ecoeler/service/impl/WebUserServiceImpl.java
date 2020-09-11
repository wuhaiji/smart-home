package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebUserService;
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
public class WebUserServiceImpl extends ServiceImpl<WebUserMapper, WebUser> implements IWebUserService {


}

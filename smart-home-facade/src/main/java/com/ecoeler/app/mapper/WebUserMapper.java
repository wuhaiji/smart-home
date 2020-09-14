package com.ecoeler.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebUser;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface WebUserMapper extends BaseMapper<WebUser> {

    List<WebRoleBean> selectUserCountByRoleId();
}

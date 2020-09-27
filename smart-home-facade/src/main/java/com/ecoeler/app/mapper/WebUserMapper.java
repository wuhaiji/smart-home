package com.ecoeler.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.bean.v1.WebUserBean;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Mapper
public interface WebUserMapper extends BaseMapper<WebUser> {
    /**
     * 查询角色对应的用户数量
     * @return
     */
    List<WebRoleBean> selectUserCountByRoleId();

    /**
     * 按条件查询用户信息
     * @param dto
     * @return
     */
    List<WebUserBean> selectUserList(@Param("dto") WebUserDto dto);
}

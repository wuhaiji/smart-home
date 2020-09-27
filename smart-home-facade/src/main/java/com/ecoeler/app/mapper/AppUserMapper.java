package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

}

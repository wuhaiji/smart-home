package com.ecoeler.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.entity.WebRolePermission;
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
public interface WebRolePermissionMapper extends BaseMapper<WebRolePermission> {
    /**
     * 批量插入
     * @param list
     */
    void batchInsert(@Param("list") List<WebRolePermission> list);
}

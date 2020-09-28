package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.entity.DeviceData;
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
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
    /**
     * 批量插入数据
     * @param list 数据
     */
    void insertBatch(@Param("list") List<DeviceData> list);
}

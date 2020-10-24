package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.bean.v1.DeviceBean;
import com.ecoeler.app.entity.Device;
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
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 查询房间下的设备
     * @param roomId
     * @return
     */
    List<DeviceBean> selectListRoomDevice(@Param("roomId") Long roomId);

    /**
     * 查询家庭下的设备
     * @param familyId
     * @return
     */
    List<DeviceBean> selectListFamilyDevice(@Param("familyId") Long familyId);
}

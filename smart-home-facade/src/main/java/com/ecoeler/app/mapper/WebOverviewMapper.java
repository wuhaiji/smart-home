package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.dto.v1.WebOverviewEchartsDto;
import com.ecoeler.app.entity.AppUser;
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
public interface WebOverviewMapper {
    /**
     * 查询echarts数据
     * @param dto  查询条件
     * @return
     */
    List<CountOfDateBean> selectEcharsData(@Param("dto") WebOverviewEchartsDto dto);
}

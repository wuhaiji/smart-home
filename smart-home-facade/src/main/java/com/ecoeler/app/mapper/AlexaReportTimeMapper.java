package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.entity.AlexaReportTime;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * alexa最后一次查询某个用户某个设备状态的时间 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-09-22
*/
@Mapper
public interface AlexaReportTimeMapper extends BaseMapper<AlexaReportTime> {

}

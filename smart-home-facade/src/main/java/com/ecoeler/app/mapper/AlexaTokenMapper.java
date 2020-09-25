package com.ecoeler.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecoeler.app.entity.AlexaToken;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * 存储alexa Statereport 和changeReport要用的amazon下发的oauth2令牌信息 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-09-22
*/
@Mapper
public interface AlexaTokenMapper extends BaseMapper<AlexaToken> {

}

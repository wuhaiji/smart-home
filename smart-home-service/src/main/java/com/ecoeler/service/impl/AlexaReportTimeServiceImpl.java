package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.AlexaReportTime;
import com.ecoeler.app.mapper.AlexaReportTimeMapper;
import com.ecoeler.app.service.IAlexaReportTimeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * alexa最后一次查询某个用户某个设备状态的时间 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-09-22
 */
@Service
public class AlexaReportTimeServiceImpl extends ServiceImpl<AlexaReportTimeMapper, AlexaReportTime> implements IAlexaReportTimeService {

}

package com.ecoeler.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.DeviceSpace;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.mapper.TimerJobMapper;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.job.JobTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
@Service
public class TimerJobServiceImpl extends ServiceImpl<TimerJobMapper, TimerJob> implements ITimerJobService {

    @Autowired
    private JobTool jobTool;

    @Override
    public Long add(TimerJob timerJob, DeviceSpace space) {

        timerJob.setType(TimerJobConst.COUNTDOWN);

        jobTool.add(timerJob.getJobCron(), JSONObject.toJSONString(timerJob));



        return null;
    }
}

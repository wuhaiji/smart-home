package com.ecoeler.service.impl;


import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.mapper.TimerJobMapper;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.job.JobTool;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public Long add(TimerJob timerJob) {
        //先增，得到 计时 任务的ID
        baseMapper.insert(timerJob);
        //开始计时，并得到 xxl-job 的ID
        ReturnT res = jobTool.add(timerJob.getJobCron(), timerJob.getId().toString() );
        //设置 xxl-job 的ID 并 记录
        timerJob.setJobId(Integer.parseInt((String)res.getContent()));
        timerJob.setNextTime(CronPatternUtil.nextDateAfter(new CronPattern(timerJob.getJobCron()),new Date(),true));
        baseMapper.updateById(timerJob);
        return timerJob.getId();
    }

    @Override
    public void updateCron(Long id,String jobCron) {
        TimerJob job = baseMapper.selectById(id);
        jobTool.update(jobCron,id.toString(),job.getJobId());
        TimerJob timerJob=new TimerJob();
        timerJob.setId(id).setJobCron(jobCron).setNextTime(new CronSequenceGenerator(jobCron).next(new Date()));
        baseMapper.updateById(timerJob);
    }

    @Override
    public void deleteJob(Long id) {
        TimerJob timerJob = baseMapper.selectById(id);
        jobTool.remove(timerJob.getJobId());
        baseMapper.deleteById(id);
    }
}

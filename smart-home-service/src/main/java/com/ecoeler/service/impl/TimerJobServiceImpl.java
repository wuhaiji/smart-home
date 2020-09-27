package com.ecoeler.service.impl;


import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.mapper.TimerJobMapper;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.job.JobTool;
import com.ecoeler.model.code.TangCode;
import com.xxl.job.core.biz.model.ReturnT;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    private IDeviceService deviceService;

    @Override
    public Long add(TimerJob timerJob) {
        Device device = deviceService.getOne(new QueryWrapper<Device>().select("family_id").eq("device_id", timerJob.getDeviceId()));
        if(device==null){
            throw new ServiceException(TangCode.DEVICE_NOT_EXISTS_ERROR);
        }
        timerJob.setFamilyId(device.getFamilyId());
        timerJob.setStatus(TimerJobConst.RUN);
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
        if(job==null){
            throw new ServiceException(TangCode.JOB_NOT_EXISTS_ERROR);
        }
        ReturnT res = jobTool.update(jobCron, id.toString(), job.getJobId());
        if(!res.success()){
            throw new ServiceException(TangCode.XXL_JOB_UPDATE_ERROR.getCode(),res.getMsg());
        }
        TimerJob timerJob=new TimerJob();
        timerJob.setId(id).setJobCron(jobCron).setNextTime(new CronSequenceGenerator(jobCron).next(new Date()));
        baseMapper.updateById(timerJob);
    }

    @Override
    public void deleteJob(Long id) {
        TimerJob timerJob = baseMapper.selectById(id);
        if(timerJob==null){
            throw new ServiceException(TangCode.JOB_NOT_EXISTS_ERROR);
        }
        ReturnT res = jobTool.stop(timerJob.getJobId());
        if(!res.success()){
            throw new ServiceException(TangCode.XXL_JOB_UPDATE_ERROR.getCode(),res.getMsg());
        }
        baseMapper.deleteById(id);
    }

    @Override
    public void stop(Long id) {
        TimerJob job = baseMapper.selectById(id);
        if(job==null){
            throw new ServiceException(TangCode.JOB_NOT_EXISTS_ERROR);
        }
        ReturnT res = jobTool.stop(job.getJobId());
        if(!res.success()){
            throw new ServiceException(TangCode.XXL_JOB_UPDATE_ERROR.getCode(),res.getMsg());
        }
        job.setStatus(TimerJobConst.PAUSE);
        baseMapper.updateById(job);
    }

    @Override
    public void start(Long id) {
        TimerJob job = baseMapper.selectById(id);
        if(job==null){
            throw new ServiceException(TangCode.JOB_NOT_EXISTS_ERROR);
        }
        ReturnT res = jobTool.start(job.getJobId());
        if(!res.success()){
            throw new ServiceException(TangCode.XXL_JOB_UPDATE_ERROR.getCode(),res.getMsg());
        }
        job.setStatus(TimerJobConst.RUN);
        baseMapper.updateById(job);
    }
}

package com.ecoeler.app.controller;


import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.feign.TimerJobService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 计时任务
 * @author tang
 * @since 2020/9/25
 */
@RestController
@RequestMapping("/timer-job")
public class TimerJobController {

    @Autowired
    private TimerJobService timerJobService;

    /**
     * 新增倒计时任务
     * @param timerJob
     * @return
     */
    @RequestMapping("/add/countdown")
    public Result addCountdown(TimerJob timerJob,Integer delay){
        timerJob.setNextTime(null);
        timerJob.setJobCron(DateFormatUtils.format(System.currentTimeMillis()+delay*1000,"ss mm HH dd MM ? yyyy"));
        timerJob.setType(TimerJobConst.COUNTDOWN);

        ExceptionUtil.notBlank(timerJob.getDeviceId(),TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(timerJob.getCmd(),TangCode.CODE_CMD_EMPTY_ERROR);
        ExceptionUtil.notBlank(timerJob.getProductId(), TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);

        return timerJobService.add(timerJob);
    }


    /**
     * 新增定时任务
     * @param timerJob
     * @return
     */
    @RequestMapping("/add/timer")
    public Result addCountdown(TimerJob timerJob){
        timerJob.setNextTime(null);
        timerJob.setType(TimerJobConst.TIMING);

        ExceptionUtil.notBlank(timerJob.getDeviceId(),TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(timerJob.getJobCron(),TangCode.CODE_CRON_EMPTY_ERROR);
        ExceptionUtil.notBlank(timerJob.getCmd(),TangCode.CODE_CMD_EMPTY_ERROR);
        ExceptionUtil.notBlank(timerJob.getProductId(), TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);

        return timerJobService.add(timerJob);
    }


    /**
     * 更新任务信息（不涉及时间）
     * @param timerJob
     * @return
     */
    @RequestMapping("/update")
    public Result update(TimerJob timerJob){
        timerJob.setType(null);
        timerJob.setJobCron(null);
        timerJob.setNextTime(null);
        ExceptionUtil.notNull(timerJob.getId(),TangCode.CODE_TIMER_JOB_ID_NULL_ERROR);
        return timerJobService.update(timerJob);
    }

    /**
     * 更新任务的cron！
     * @param id
     * @param jobCron
     * @return
     */
    @RequestMapping("/update/cron")
    public Result updateCron(Long id, String jobCron){
        ExceptionUtil.notNull(id,TangCode.CODE_TIMER_JOB_ID_NULL_ERROR);
        ExceptionUtil.notBlank(jobCron,TangCode.CODE_CRON_EMPTY_ERROR);
        return timerJobService.updateCron(id,jobCron);
    }

    /**
     * 删除计时任务
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_TIMER_JOB_ID_NULL_ERROR);
        return  timerJobService.delete(id);
    }


    @RequestMapping("/stop")
    public Result stop(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_TIMER_JOB_ID_NULL_ERROR);
        return timerJobService.stop(id);
    }

    @RequestMapping("/start")
    public Result start(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_TIMER_JOB_ID_NULL_ERROR);
        return timerJobService.start(id);
    }



    @RequestMapping("/list/countdown")
    public Result listCountdown(String deviceId){
        ExceptionUtil.notBlank(deviceId,TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        return timerJobService.listCountdown(deviceId);
    }

    @RequestMapping("/list/timer")
    public Result listTimer(String deviceId){
        ExceptionUtil.notBlank(deviceId,TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        return timerJobService.listTimer(deviceId);
    }

    @RequestMapping("/list/axis")
    public Result listAxis(Long familyId){
        ExceptionUtil.notNull(familyId,TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return timerJobService.listAxis(familyId);
    }

}

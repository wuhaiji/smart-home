package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.service.ITimerJobService;

import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.model.response.Result;
import com.xxl.job.core.util.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;


/**
 * 任务控制器
 * @author tang
 * @since 2020/9/25
 */
@RestController
@RequestMapping("/timer-job")
public class TimerJobController {

    @Autowired
    private ITimerJobService timerJobService;

    /**
     * 新增计时任务
     * @param timerJob
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody TimerJob timerJob){
        return Result.ok(timerJobService.add(timerJob));
    }

    /**
     * 更新任务信息（不涉及时间）
     * @param timerJob
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody TimerJob timerJob){
        return Result.ok(timerJobService.updateById(timerJob));
    }

    /**
     * 更新任务的cron！
     * @param id
     * @param jobCron
     * @return
     */
    @PostMapping("/update/cron")
    public Result updateCron(@RequestParam Long id,@RequestParam String jobCron){
        timerJobService.updateCron(id,jobCron);
        return Result.ok();
    }

    /**
     * 删除计时任务
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id){
        timerJobService.deleteJob(id);
        return Result.ok();
    }

    /**
     * 停止 任务
     * @param id
     * @return
     */
    @PostMapping("/stop")
    public Result stop(@RequestParam Long id){
        timerJobService.stop(id);
        return Result.ok();
    }

    /**
     * 开启任务
     * @param id
     * @return
     */
    @PostMapping("/start")
    public Result start(@RequestParam Long id){
        timerJobService.start(id);
        return Result.ok();
    }



    @PostMapping("/list/countdown")
    public Result listCountdown(@RequestParam String deviceId){
        return Result.ok(timerJobService.list(
                new QueryWrapper<TimerJob>()
                        .eq("device_id",deviceId)
                        .eq("status", TimerJobConst.RUN)
                        .eq("type",TimerJobConst.COUNTDOWN)
        ));
    }

    @PostMapping("/list/timer")
    public Result listTimer(@RequestParam String deviceId){
        return Result.ok(timerJobService.list(
                new QueryWrapper<TimerJob>()
                        .eq("device_id",deviceId)
                        .eq("status", TimerJobConst.RUN)
                        .eq("type",TimerJobConst.TIMING)
        ));
    }


    @PostMapping("/list/axis")
    public Result listAxis(@RequestParam Long familyId){
        Date date = new Date();
        return Result.ok(timerJobService.list(
                new QueryWrapper<TimerJob>()
                        .eq("family_id",familyId)
                        .eq("status", TimerJobConst.RUN)
                        //取未来两天的时间轴
                        .between("next_time",date,DateUtils.addDays(DateUtils.ceiling(date, Calendar.DAY_OF_MONTH),2))
                        .orderByDesc("next_time")
        ));
    }

}

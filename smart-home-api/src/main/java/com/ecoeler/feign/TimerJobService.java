package com.ecoeler.feign;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.model.response.Result;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;


@FeignClient(value = "smart-home-service", path = "/timer-job",contextId = "timer-job")
public interface TimerJobService {

    @PostMapping("/add")
    Result<Long> add(@RequestBody TimerJob timerJob);

    @PostMapping("/update")
    Result update(@RequestBody TimerJob timerJob);

    @PostMapping("/update/cron")
    Result updateCron(@RequestParam Long id , @RequestParam String jobCron);

    @PostMapping("/delete")
    Result delete(@RequestParam Long id);

    @PostMapping("/stop")
    Result stop(@RequestParam Long id);

    @PostMapping("/start")
    Result start(@RequestParam Long id);

    @PostMapping("/list/countdown")
    Result listCountdown(@RequestParam String deviceId);

    @PostMapping("/list/timer")
    Result listTimer(@RequestParam String deviceId);

    @PostMapping("/list/axis")
    Result listAxis(@RequestParam Long familyId);

}

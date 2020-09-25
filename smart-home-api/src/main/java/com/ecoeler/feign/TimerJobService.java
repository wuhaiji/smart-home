package com.ecoeler.feign;


import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


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

}

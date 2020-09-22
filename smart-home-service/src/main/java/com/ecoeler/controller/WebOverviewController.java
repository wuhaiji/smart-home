package com.ecoeler.controller;

import com.ecoeler.app.service.IWebDeviceService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangcx
 * @since 2020-9-22
 */
@Slf4j
@RequestMapping("/web_overview")
@RestController
public class WebOverviewController {
    @Autowired
    private IWebDeviceService iWebDeviceService;

    /**
     * 查询总的设备数量
     * @return
     */
    @RequestMapping("query/total/device/count")
    public Result queryDeviceTotalCount(){
        log.info("smart-home-service->WebOverviewController->begin query total device count ");
        Integer total=iWebDeviceService.selectDeviceTotalCount();
        return Result.ok(total);
    }
    /**
     * 查询今天设备数量
     * @return
     */
    @RequestMapping("query/today/device/count")
    public Result queryDeviceTodayCount(){
        log.info("smart-home-service->WebOverviewController->begin query today device count ");
        Integer today=iWebDeviceService.selectDeviceTodayCount();
        return Result.ok(today);
    }
    /**
     * 查询设备较昨日的日环比
     * @return
     */
    @RequestMapping("query/device/day/compare")
    public Result queryDeviceDayCompare(){
        log.info("smart-home-service->WebOverviewController->begin query device day compare");
        float ratio=iWebDeviceService.selectDeviceDayCompare();
        return Result.ok(ratio);
    }


}

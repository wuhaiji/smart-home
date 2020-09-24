package com.ecoeler.controller;

import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.bean.v1.WebOverviewDataStatisticsBean;


import com.ecoeler.app.dto.v1.QueryDateDto;
import com.ecoeler.app.dto.v1.QueryTimeDto;
import com.ecoeler.app.service.IWebCustomerService;
import com.ecoeler.app.service.IWebDeviceService;
import com.ecoeler.app.service.WebOverviewDataService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tangcx
 * @since 2020-9-22
 */
@Slf4j
@RequestMapping("/web_overview")
@RestController
public class WebOverviewController {

    @Autowired
    private WebOverviewDataService webOverviewDataService;

    /**
     * 查询数据统计
     *
     * @return 统计数据
     */
    @RequestMapping("query/overview/data/statistics")
    public Result queryOverviewDataStatistics() {
        log.info("smart-home-service->WebOverviewController->begin query overview statistics ");
        WebOverviewDataStatisticsBean result = webOverviewDataService.getDataStatistics();
        return Result.ok(result);
    }

    /**
     * 查询设备echarts数据
     *
     * @param queryDateDto 查询时间段
     * @return echarts数据 时间段内每天新增注册设备数量
     */
    @RequestMapping("query/device/echarts")
    public Result queryOverviewDeviceDataStatistics(@RequestBody QueryDateDto queryDateDto) {
        log.info("smart-home-service->WebOverviewController->begin query device echarts");
        List<CountOfDateBean> result = webOverviewDataService.getDeviceEcharts(queryDateDto);
        return Result.ok(result);
    }

    /**
     * 查询App用户echarts数据
     *
     * @param queryDateDto 查询时间段
     * @return echarts数据 时间段内每天新增注册人员数量
     */
    @RequestMapping("query/app/user/echarts")
    public Result queryAppUserOverviewDeviceDataStatistics(@RequestBody QueryDateDto queryDateDto) {
        log.info("smart-home-service->WebOverviewController->begin query appUser echarts");
        List<CountOfDateBean> result = webOverviewDataService.getAppUserEcharts(queryDateDto);
        return Result.ok(result);
    }
}

package com.ecoeler.web.controller;

import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.bean.v1.WebOverviewDataStatisticsBean;
import com.ecoeler.app.dto.v1.QueryDateDto;
import com.ecoeler.feign.WebOverviewService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 概览端口
 *
 * @author tangcx
 * @since 2020/9/23
 */
@Slf4j
@RestController
@RequestMapping("/web/overview")
public class OverviewController {
    @Autowired
    private WebOverviewService webOverviewService;

    /**
     * 查询数据统计
     *
     * @return
     */
    @RequestMapping("query/overview/data/statistics")
    public Result queryOverviewDataStatistics() {
        log.info("smart-home-web->OverviewController->begin query overview statistics ");
        return webOverviewService.queryOverviewDataStatistics();
    }

    /**
     * 查询设备echarts数据
     * @param queryDateDto 查询条件
     * @return
     */
    @RequestMapping("query/device/echarts")
    public Result queryOverviewDeviceDataStatistics(QueryDateDto queryDateDto) {
        log.info("smart-home-web->OverviewController->->begin query device echarts");
        return webOverviewService.queryOverviewDeviceDataStatistics(queryDateDto);
    }

    /**
     * 查询设备echarts数据
     * @param queryDateDto 查询条件
     * @return
     */
    @RequestMapping("query/customer/echarts")
    public Result queryOverviewAppUserDataStatistics(QueryDateDto queryDateDto) {
        log.info("smart-home-web->OverviewController->->begin query appUser echarts");
        return webOverviewService.queryAppUserOverviewDeviceDataStatistics(queryDateDto);
    }

}

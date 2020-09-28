package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.QueryDateDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tangcx
 */
@FeignClient(value = "smart-home-service", path = "/web_overview", contextId = "overview")
public interface WebOverviewService {
    /**
     * 查询数据统计
     *
     * @return
     */
    @RequestMapping("query/overview/data/statistics")
    Result queryOverviewDataStatistics();

    /**
     * 查询设备echarts数据
     *
     * @param queryDateDto 查询时间
     * @return
     */
    @RequestMapping("query/device/echarts")
    Result queryOverviewDeviceDataStatistics(@RequestBody QueryDateDto queryDateDto);

    /**
     * 查询App用户echarts数据
     *
     * @param queryDateDto 查询时间
     * @return
     */
    @RequestMapping("query/app/user/echarts")
    Result queryAppUserOverviewDeviceDataStatistics(@RequestBody QueryDateDto queryDateDto);



}

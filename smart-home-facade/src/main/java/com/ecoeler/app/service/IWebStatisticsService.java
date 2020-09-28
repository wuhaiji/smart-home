package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.bean.v1.WebOverviewDataStatisticsBean;
import com.ecoeler.app.dto.v1.QueryDateDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebStatistics;

import java.util.List;

/**
 * @author tangcx
 * @since 2020-9-22
 */
public interface IWebStatisticsService extends IService<WebStatistics> {
    /**
     * 查询统计数据
     * @return
     */
    WebOverviewDataStatisticsBean getDataStatistics() ;

    /**
     * 查询设备echarts
     * @param queryTimeDto
     * @return
     */
    List<WebStatistics> getDeviceEcharts(QueryDateDto queryTimeDto);
    /**
     * 查询App用户echarts
     * @param queryDateDto 查询时间
     * @return
     */
    List<WebStatistics> getAppUserEcharts(QueryDateDto queryDateDto);
}

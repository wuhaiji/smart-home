package com.ecoeler.app.service;


import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.bean.v1.WebOverviewDataStatisticsBean;
import com.ecoeler.app.dto.v1.QueryDateDto;

import java.util.List;

/**
 * @author tangcx
 * @since 2020-9-22
 */
public interface WebOverviewDataService {
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
    List<CountOfDateBean> getDeviceEcharts(QueryDateDto queryTimeDto);
    /**
     * 查询App用户echarts
     * @param queryDateDto 查询时间
     * @return
     */
    List<CountOfDateBean> getAppUserEcharts(QueryDateDto queryDateDto);
}

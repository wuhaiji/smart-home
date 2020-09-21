package com.ecoeler.app.dto.v1;


import lombok.Data;

/**
 * 查询时间
 * @author tcx
 */
@Data
public class QueryTimeDto {
    /**
     * 查询时间段类型 0-online_time 1-offline_time 2-create_time 3-update_time
     */
    private Integer timeType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

}

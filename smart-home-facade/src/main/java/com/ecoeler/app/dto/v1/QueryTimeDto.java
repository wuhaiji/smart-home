package com.ecoeler.app.dto.v1;


import lombok.Data;

/**
 * 查询时间
 * @author tcx
 */
@Data
public class QueryTimeDto {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

}

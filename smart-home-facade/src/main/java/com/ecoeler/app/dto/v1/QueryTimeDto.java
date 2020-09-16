package com.ecoeler.app.dto.v1;


import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class QueryTimeDto {
    /**
     * 开始时间
     */
    public String startTime;
    /**
     * 结束时间
     */
    public String endTime;

}

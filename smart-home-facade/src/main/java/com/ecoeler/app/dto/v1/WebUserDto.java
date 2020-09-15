package com.ecoeler.app.dto.v1;

import lombok.Data;


@Data
public class WebUserDto extends QueryTimeDto {
    private String userName;
    private String email;
    private String phoneNumber;
    /**
     * 查询时间段类型
     */
    private Integer timeType;


}
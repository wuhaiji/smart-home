package com.ecoeler.app.dto.v1;

import lombok.Data;


@Data
public class WebCustomerDto extends QueryTimeDto {
    /**
     * 家庭名称
     */
    private String familyName;

    /**
     * 位置名称
     */
    private String positionName;


}

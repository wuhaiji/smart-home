package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class WebCustomerDto extends BasePageQueryTimeDto {
    /**
     * 家庭名称
     */
    private String familyName;

    /**
     * 位置名称
     */
    private String positionName;
    /**
     * 家庭类型：0别墅，1住宅
     */
    private Integer familyType;


}

package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class WebUserDto extends BasePageQueryTimeDto{
    private String userName;
    private String email;
    private String phoneNumber;



}

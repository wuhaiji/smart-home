package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author tangcx
 */
@Data
public class WebRoleBean {

    private Long id;

    private String roleName;

    private String description;

    private String role;

    private Integer count;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}

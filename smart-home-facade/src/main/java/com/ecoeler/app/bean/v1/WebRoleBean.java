package com.ecoeler.app.bean.v1;

import lombok.Data;

@Data
public class WebRoleBean {

    private Long id;

    private String roleName;

    private String description;

    private String role;

    private Integer count;
}
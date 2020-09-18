package com.ecoeler.app.dto.v1;


import lombok.Data;

import java.util.List;

@Data
public class PermDto {

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 按钮权限
     */
    private List<String> buttons;
    /**
     * 菜单权限
     */
    private List<String> permissions;


}

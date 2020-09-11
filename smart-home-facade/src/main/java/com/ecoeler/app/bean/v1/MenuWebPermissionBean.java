package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * 菜单权限
 */
@Data
public class MenuWebPermissionBean {
    /**
     * 权限id
     */
    private Long id;
    /**
     * 菜单名字
     */
    private String menu;
    /**
     * 子菜单
     */
    private List<MenuWebPermissionBean> children;
}

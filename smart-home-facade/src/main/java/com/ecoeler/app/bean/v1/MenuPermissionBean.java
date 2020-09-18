package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * @author tangcx
 * 渲染前端全部菜单权限
 */
@Data
public class MenuPermissionBean {
    /**
     * 权限id
     */
    private Long id;
    /**
     * 菜单权限
     */
    private String menuPermission;
    /**
     * 菜单名字
     */
    private String menuPermissionName;
    /**
     * 子菜单
     */
    private List<MenuPermissionBean> children;

    public MenuPermissionBean() {
    }

    public MenuPermissionBean(Long id, String menuPermission) {
        this.id = id;
        this.menuPermission = menuPermission;
    }

    public MenuPermissionBean(Long id, String menuPermission, String menuPermissionName) {
        this.id = id;
        this.menuPermission = menuPermission;
        this.menuPermissionName = menuPermissionName;
    }
}

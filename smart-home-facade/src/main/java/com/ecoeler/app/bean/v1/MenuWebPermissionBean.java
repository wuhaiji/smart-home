package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * @author tangcx
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

    public MenuWebPermissionBean() {
    }

    public MenuWebPermissionBean(Long id, String menu) {
        this.id = id;
        this.menu = menu;
    }
}

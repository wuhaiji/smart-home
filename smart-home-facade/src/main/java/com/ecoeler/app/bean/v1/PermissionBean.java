package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * @author tangcx
 */
@Data
public class PermissionBean {
    /**
     * 菜单权限
     */
    List<MenuPermissionBean> menus;

    /**
     * 按钮权限
     */
    List<ButtonWebPermissionBean> buttons;

}

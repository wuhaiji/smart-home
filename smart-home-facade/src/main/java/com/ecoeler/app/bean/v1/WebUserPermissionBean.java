package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
  * @author tcx
 */
@Data
public class WebUserPermissionBean {
    /**
     * 按钮权限
     */
    private List<String> buttons;

    /**
     * 菜单权限
     */
    private List<MenuWebPermissionBean> menus;

}

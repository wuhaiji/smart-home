package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
  * @author tcx
 */
@Data
public class WebEchoUserPermissionBean {
    /**
     * 按钮权限
     */
    private List<String> buttons;

    /**
     * 菜单权限
     */
    private List<Long> menus;

}

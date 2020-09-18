package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * 按钮权限 按钮全部权限的封装
 * 渲染前端 全部按钮权限
 * @author tangcx
 */
@Data
public class ButtonWebPermissionBean {
    /**
     * 父菜单名字
     */
    private String parentMenuName;

    /**
     * 按钮
     */
    private List<ButtonPermissionBean> buttons;


}

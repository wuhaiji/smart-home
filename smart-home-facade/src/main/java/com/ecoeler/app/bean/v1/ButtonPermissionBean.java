package com.ecoeler.app.bean.v1;

import lombok.Data;

/**
 * @author tangcx
 * 菜单权限
 */
@Data
public class ButtonPermissionBean {
    /**
     * 权限id
     */
    private Long id;
    /**
     * 权限
     */
    private String permission;
    /**
     * 按钮名字
     */
    private String buttonName;

    public ButtonPermissionBean(Long id) {
        this.id = id;
    }

    public ButtonPermissionBean(Long id, String buttonName) {
        this.id = id;
        this.buttonName = buttonName;
    }
}

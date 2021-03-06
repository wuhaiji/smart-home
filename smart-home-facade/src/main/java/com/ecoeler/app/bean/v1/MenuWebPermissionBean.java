package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * 查询当前用户页面权限的封装
 * @author tangcx
 */
@Data
public class MenuWebPermissionBean {

    /**
     * 父菜单名字
     */
    private String parentMenuName;
    /**
     *按钮
     */
    private List<String> childrenNames;

}

package com.ecoeler.controller;


import com.ecoeler.model.response.Result;
import com.ecoeler.service.IWebRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Controller
@RequestMapping("/web-role-permission")
public class WebRolePermissionController {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;
    /**
     * 定制权限
     * @param permissionIds
     * @return
     */
    @RequestMapping("customization")
    public Result deleteRole(List<Long> permissionIds,Long roleId){
        iWebRolePermissionService.customizationPermission(permissionIds,roleId);
        return Result.ok();
    }
}
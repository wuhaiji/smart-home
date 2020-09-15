package com.ecoeler.controller;


import com.ecoeler.model.response.Result;
import com.ecoeler.app.service.IWebRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@RestController
@RequestMapping("/web-role-permission")
public class WebRolePermissionController {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;

    /**
     * 定制权限
     *
     * @param permissionIds
     * @return
     */
    @RequestMapping("customization")
    public Result deleteRole(@RequestParam(value = "permissionIds") List<Long> permissionIds, Long roleId) {
        iWebRolePermissionService.customizationPermission(permissionIds, roleId);
        return Result.ok();
    }
}

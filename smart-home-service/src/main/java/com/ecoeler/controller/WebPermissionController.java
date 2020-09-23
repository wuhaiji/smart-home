package com.ecoeler.controller;


import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Slf4j
@RestController
@RequestMapping("/web-permission")
public class WebPermissionController {
    @Autowired
    private IWebPermissionService iWebPermissionService;

    /***
     * 查询所有权限
     * @return
     */
    @RequestMapping("/query/all")
    public Result queryAllPermission() {
        log.info("smart-home-service->WebPermissionController->begin query all permissions");
        return Result.ok(iWebPermissionService.selectAllPermission());
    }

    /***
     * 根据用户信息获取权限
     * @return
     */
    @RequestMapping("/query/web/permission")
    public Result queryWebPermission(@RequestParam Long userId) {
        log.info("smart-home-service->WebPermissionController->begin query web permission for webUser");
        return Result.ok(iWebPermissionService.selectWebPermissionByUserId(userId));
    }

    /***
     * 根据userId权限拦截 后台控制权限
     * @return
     */
    @RequestMapping("/query/by/user/id")
    public Result queryPermissionByUserId(@RequestParam Long userId) {
        log.info("smart-home-service->WebPermissionController->begin query back permission for webUser");
        return Result.ok(iWebPermissionService.getPerByUserId(userId));
    }

    /***
     * 根据roleId获取回显权限
     * @return
     */
    @RequestMapping("/query/echo/by/role/id")
    public Result queryEchoPermissionByRoleId(@RequestParam Long roleId) {
        log.info("smart-home-service->WebPermissionController->begin query echo permission for role");
        return Result.ok(iWebPermissionService.selectEchoPermissionByRoleId(roleId));
    }

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 定制的权限 及角色id
     * @return
     */
    @RequestMapping("customization")
    public Result customizationPermission(@RequestBody CustomizationPermissionDto customizationPermissionDto) {
        log.info("smart-home-service->WebPermissionController->begin customization permission for role");
        iWebPermissionService.customizationPermission(customizationPermissionDto);
        return Result.ok();
    }

}

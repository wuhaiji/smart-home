package com.ecoeler.web.controller;


import com.ecoeler.app.dto.v1.BasePageDto;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.dto.v1.PermDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.feign.WebRoleService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;


/**
 * 角色端口
 *
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/role")
public class RoleController {
    @Autowired
    private WebRoleService webRoleService;

    /**
     * 新增角色
     *
     * @param webRole 角色信息
     * @return
     */
    @RequestMapping("save")
    public Result saveRole(WebRole webRole) {
        log.info("smart-home-web->RoleController->begin save role");
        return webRoleService.saveRole(webRole);
    }

    /**
     * 修改角色
     *
     * @param webRole
     * @return
     */
    @RequestMapping("update")
    public Result updateRole(WebRole webRole) {
        log.info("smart-home-web->RoleController->begin update role");
        return webRoleService.updateRole(webRole);
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public Result deleteRole(Long id) {
        log.info("smart-home-web->RoleController->begin delete role");
        return webRoleService.deleteRole(id);
    }

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("query/list")
    public Result queryRoleList(BasePageDto basePageDto) {
        log.info("smart-home-web->RoleController->begin query all roles");
        return webRoleService.queryRoleList(basePageDto);
    }
    /***
     * 查询所有权限
     * @return
     */
    @RequestMapping("/query/all/permission")
    public Result queryAllMenuPermission() {
        log.info("smart-home-web->RoleController->begin query all permissions");
        return webRoleService.queryAllPermission();
    }

    /***
     * 根据用户信息获取权限
     * @return
     */
    @RequestMapping("/query/web/permission")
    public Result queryWebPermission(Principal principal) {
        log.info("smart-home-web->RoleController->begin query permissions for login webUser");
        return webRoleService.queryWebPermission(Long.parseLong(principal.getName()));
    }

    /***
     * 根据roleId获取回显权限
     * @return
     */
    @RequestMapping("/query/echo/by/role/id")
    public Result queryEchoPermissionByRoleId(Long roleId) {
        log.info("smart-home-web->RoleController->begin query echo permissions for role");
        return webRoleService.queryEchoPermissionByRoleId(roleId);
    }

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 定制的权限 及角色id
     * @return
     */
    @RequestMapping("customization")
    public Result customizationPermission(CustomizationPermissionDto customizationPermissionDto) {
        log.info("smart-home-web->RoleController->begin customization permissions for role");
        return webRoleService.customizationPermission(customizationPermissionDto);
    }

    /**
     * 查询所有角色列表选择框
     *
     * @return
     */
    @RequestMapping("query/combo/box/role/list")
    public Result queryRoleListExceptById() {
        log.info("smart-home-web->RoleController->begin query all combo box role list");
        return webRoleService.queryComboBoxRoleList();
    }




}

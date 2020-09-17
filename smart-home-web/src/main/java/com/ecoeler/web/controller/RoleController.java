package com.ecoeler.web.controller;


import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
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
    public Result addRole(WebRole webRole) {
        return webRoleService.addRole(webRole);
    }

    /**
     * 修改角色
     *
     * @param webRole
     * @return
     */
    @RequestMapping("update")
    public Result updateRole(WebRole webRole) {
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
        return webRoleService.deleteRole(id);
    }

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("query/list")
    public Result queryRoleList() {
        return webRoleService.queryRoleList();
    }

    /**
     * 查询不是当前用户角色列表
     *
     * @return
     */
    @RequestMapping("query/list/except/by/id")
    public Result queryRoleListExceptById(Long roleId) {
        return webRoleService.queryRoleListExceptById(roleId);
    }

    /***
     * 查询所有菜单权限
     * @return
     */
    @RequestMapping("/query/all/menu/permission")
    public Result queryAllMenuPermission() {
        log.info("开始查询所有菜单权限");
        return webRoleService.queryAllMenuPermission();
    }

    /***
     * 根据用户信息获取权限
     * @return
     */
    @RequestMapping("/query/web/permission")
    public Result queryWebPermission(Principal principal) {
        log.info("开始根据用户信息 获取用户权限");
        return webRoleService.queryWebPermission(Long.parseLong(principal.getName()));
    }

    /***
     * 根据roleId获取回显权限
     * @return
     */
    @RequestMapping("/query/echo/by/role/id")
    public Result queryEchoPermissionByRoleId(Long roleId) {
        log.info("开始根据角色Id 获取用户回显权限");
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
        log.info("开始制定权限");
        log.error(LocalDateTime.now().toString());
        return webRoleService.customizationPermission(customizationPermissionDto);
    }

}

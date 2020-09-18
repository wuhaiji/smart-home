package com.ecoeler.controller;


import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.model.response.Result;
import com.ecoeler.app.service.IWebRoleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ListResourceBundle;


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
@RequestMapping("/web-role")
public class WebRoleController {
    @Autowired
    private IWebRoleService iWebRoleService;

    /**
     * 新增角色
     *
     * @param webRole 角色信息
     * @return
     */
    @RequestMapping("save")
    public Result saveRole(@RequestBody WebRole webRole) {
        log.info("smart-home-service->WebRoleController->begin save role");
        Long roleId = iWebRoleService.addRole(webRole);
        return Result.ok(roleId);
    }

    /**
     * 修改角色
     *
     * @param webRole
     * @return
     */
    @RequestMapping("update")
    public Result updateRole(@RequestBody WebRole webRole) {
        log.info("smart-home-service->WebRoleController->begin update role");
        iWebRoleService.updateRole(webRole);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public Result deleteRole(@RequestBody Long id) {
        log.info("smart-home-service->WebRoleController->begin delete role");
        iWebRoleService.deleteRole(id);
        return Result.ok();
    }

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("query/list")
    public Result queryRoleList() {
        log.info("smart-home-service->WebRoleController->begin query role list");
        List<WebRoleBean> result = iWebRoleService.selectRoleList();
        return Result.ok(result);
    }

    /**
     * 查询不是当前用户角色列表
     *
     * @return
     */
    @RequestMapping("query/list/except/by/id")
    public Result queryRoleListExceptById(@RequestParam Long roleId) {
        log.info("smart-home-service->WebRoleController->begin query other role list");
        List<WebRole> result = iWebRoleService.selectRoleListExceptById(roleId);
        return Result.ok(result);
    }


}

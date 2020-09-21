package com.ecoeler.controller;


import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.BasePageDto;
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
     * @param basePageDto 分页
     * @return
     */
    @RequestMapping("query/list")
    public Result queryRoleList(@RequestBody BasePageDto basePageDto) {
        log.info("smart-home-service->WebRoleController->begin query role list");
        PageBean<WebRoleBean> result = iWebRoleService.selectRoleList(basePageDto);
        return Result.ok(result);
    }

    /**
     * 查询角色列表下拉选择框
     *
     * @return
     */
    @RequestMapping("query/combo/box")
    public Result queryComboBoxRoleList() {
        log.info("smart-home-service->WebRoleController->begin query combo box role list");
        List<WebRole> result = iWebRoleService.selectRoleComboBoxRoleList();
        return Result.ok(result);
    }


}

package com.ecoeler.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.feign.WebUserService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端口
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/user")
public class UserController {
    @Autowired
    private WebUserService webUserService;
    @RequestMapping("/user")
    public Result user(@RequestParam String account){
        log.info("smart-home-web->UserController->begin query webUser by account");
        return webUserService.getUser(account);
    }
    /**
     * 新增用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("/save")
    public Result saveWebUser(WebUser webUser) {
        log.info("smart-home-web->UserController->begin save webUser");
        return webUserService.saveWebUser(webUser);
    }
    /**
     * 修改用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("/update")
    public Result updateWebUser(WebUser webUser) {
        log.info("smart-home-web->UserController->begin update webUser");
        return webUserService.updateWebUser(webUser);
    }
    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result deleteWebUser(Long id) {
        log.info("smart-home-web->UserController->begin delete webUser");
        return webUserService.deleteWebUser(id);
    }

    /**
     * 查询用户列表
     *
     * @param webUserDto 查询条件
     * @return
     */
    @RequestMapping("/query/list")
    public Result queryWebUserList(WebUserDto webUserDto) {
        log.info("smart-home-web->UserController->begin query webUser list");
        return webUserService.queryWebUserList(webUserDto);
    }

    /**
     * 分配角色
     *
     * @param allocationRoleDto 用户角色信息
     * @return
     */
    @RequestMapping("/allocation/role")
    public Result allocationWebUserRole(AllocationRoleDto allocationRoleDto) {
        log.info("smart-home-web->UserController->begin allocation role for webUser");
        return  webUserService.allocationWebUserRole(allocationRoleDto);
    }

    /***
     * 根据userId权限拦截 后台控制权限
     * @return
     */
    @RequestMapping("/query/by/user/id")
    public Result queryPermissionByUserId(Long userId) {
        log.info("smart-home-web->UserController->begin query back permission for webUser");
        return webUserService.getPerm(userId);
    }

}

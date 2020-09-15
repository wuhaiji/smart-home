package com.ecoeler.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/web-user")
public class WebUserController {
    @Autowired
    private IWebUserService iWebUserService;

    /**
     * 新增用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("save")
    public Result saveWebUser(WebUser webUser) {
        log.info("开始新增用户");
        Long id = iWebUserService.addWebUser(webUser);
        return Result.ok(id);
    }

    /**
     * 修改用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("update")
    public Result updateWebUser(WebUser webUser) {
        log.info("开始修改用户");
        iWebUserService.updateWebUser(webUser);
        return Result.ok();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public Result deleteWebUser(Long id) {
        log.info("开始删除用户");
        iWebUserService.deleteWebUser(id);
        return Result.ok();
    }

    /**
     * 查询用户列表
     *
     * @param webUserDto 查询条件
     * @param page       分页
     * @return
     */
    @RequestMapping("query/list")
    public Result queryWebUserList(WebUserDto webUserDto, Page<WebUser> page) {
        log.info("开始分页查询用户列表");
        PageBean<WebUser> result = iWebUserService.queryWebUserList(webUserDto, page);
        return Result.ok(result);
    }

    /**
     * 分配角色
     *
     * @param userId  用户id
     * @param webRole 角色
     * @return
     */
    @RequestMapping("allocation/role")
    public Result allocationWebUserRole(Long userId, WebRole webRole) {
        log.info("开始给用户分配角色");
        iWebUserService.allocationWebUserRole(userId, webRole);
        return Result.ok();
    }


}

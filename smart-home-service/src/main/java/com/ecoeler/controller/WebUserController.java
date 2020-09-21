package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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

    @RequestMapping("/user")
    public Result user(@RequestParam String account){
        QueryWrapper<WebUser> q=new QueryWrapper<>();
        q.eq("email",account).or().eq("phone_number",account);
        return Result.ok(iWebUserService.getOne(q));
    }


    /**
     * 新增用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("save")
    public Result saveWebUser(@RequestBody WebUser webUser) {
        log.info("smart-home-service->WebUserController->begin save webUser");
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
    public Result updateWebUser( @RequestBody WebUser webUser) {
        log.info("smart-home-service->WebUserController->begin update webUser");
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
    public Result deleteWebUser(@RequestParam Long id) {
        log.info("smart-home-service->WebUserController->begin delete webUser");
        iWebUserService.deleteWebUser(id);
        return Result.ok();
    }

    /**
     * 查询用户列表
     *
     * @param webUserDto 查询条件
     * @return
     */
    @RequestMapping("query/list")
    public Result queryWebUserList(@RequestBody WebUserDto webUserDto) {
        log.info("smart-home-service->WebUserController->begin query webUser list");
        PageBean<WebUser> result = iWebUserService.queryWebUserList(webUserDto);
        return Result.ok(result);
    }

    /**
     * 分配角色
     *
     * @param allocationRoleDto  用户id 角色信息
     * @return
     */
   /* @RequestMapping("allocation/role")
    public Result allocationWebUserRole(@RequestBody AllocationRoleDto allocationRoleDto) {
        log.info("smart-home-service->WebUserController->begin allocation role for webUser");
        iWebUserService.allocationWebUserRole(allocationRoleDto);
        return Result.ok();
    }*/

}

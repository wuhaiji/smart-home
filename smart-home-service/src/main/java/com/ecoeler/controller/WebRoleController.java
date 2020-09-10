package com.ecoeler.controller;


import com.ecoeler.entity.WebRole;
import com.ecoeler.model.response.Result;
import com.ecoeler.service.IWebRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Controller
@RequestMapping("/web-role")
public class WebRoleController {
    @Autowired
    private IWebRoleService iWebRoleService;

    /**
     * 新增角色
     * @param webRole
     * @return
     */
    @RequestMapping("save")
    public Result addRole(WebRole webRole){
        Long roleId=iWebRoleService.addRole(webRole);
        return Result.ok(roleId);
    }
    /**
     * 修改角色
     * @param webRole
     * @return
     */
    @RequestMapping("update")
    public Result updateRole(WebRole webRole){
        iWebRoleService.updateRole(webRole);
        return Result.ok();
    }
    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public Result deleteRole(Long id){
        iWebRoleService.deleteRole(id);
        return Result.ok();
    }


}

package com.ecoeler.feign;


import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.dto.v1.PermDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.service.IWebRoleService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * feign
 *
 * @author tang
 * @since 2020-09-10
 */


@FeignClient(name = "smart-home-service", contextId = "webRole")
public interface WebRoleService {

    /**
     * 新增角色
     *
     * @param webRole 角色信息
     * @return
     */
    @RequestMapping("/web-role/save")
    Result addRole(@RequestBody WebRole webRole);

    /**
     * 修改角色
     *
     * @param webRole
     * @return
     */
    @RequestMapping("/web-role/update")
    Result updateRole(@RequestBody WebRole webRole);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/web-role/delete")
    Result deleteRole(@RequestBody Long id);

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("/web-role/query/list")
    Result queryRoleList();

    /**
     * 查询不是当前用户角色列表
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/web-role/query/list/except/by/id")
    Result queryRoleListExceptById(@RequestParam Long roleId);


    /***
     * 查询所有菜单权限
     * @return
     */
    @RequestMapping("/web-permission/query/all/menu/permission")
    Result queryAllMenuPermission();

    /***
     * 根据用户信息获取权限
     * @param userId 用户Id
     * @return
     */
    @RequestMapping("/web-permission//query/web/permission")
    Result queryWebPermission(@RequestParam Long userId);

    /***
     * 根据userId权限拦截 后台控制权限
     *  @param userId 用户Id
     * @return
     */
   /* @RequestMapping("/web-permission/query/by/user/id")
    Result queryPermissionByUserId(@RequestParam Long userId);*/

    /***
     * 根据roleId获取回显权限
     * @param roleId 角色Id
     * @return
     */
    @RequestMapping("/web-permission/query/echo/by/role/id")
    Result queryEchoPermissionByRoleId(@RequestParam Long roleId);

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 定制的权限 及角色id
     * @return
     */
    @RequestMapping("/web-role-permission/customization")
    Result customizationPermission(@RequestBody CustomizationPermissionDto customizationPermissionDto);


    @RequestMapping("/web-role-permission/change/role/permission")
    Result changeRolePermission(@RequestBody PermDto dto);


}

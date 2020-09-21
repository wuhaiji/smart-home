package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * web user
 *
 * @author tang
 * @since 2020/9/16
 */
@FeignClient(value = "smart-home-service", contextId = "web")
public interface WebUserService {

    /**
     * 根据账号信息查询USER
     *
     * @param account 可能为邮箱、可能为手机号
     * @return
     */
    @PostMapping("/web-user/user")
    Result<WebUser> getUser(@RequestParam String account);

    /**
     * 获得用户的权限列表
     *
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/web-permission/query/by/user/id")
    Result<Set<String>> getPerm(@RequestParam Long userId);

    /**
     * 新增用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("/web-user/save")
    Result saveWebUser(@RequestBody WebUser webUser);

    /**
     * 修改用户
     *
     * @param webUser
     * @return
     */
    @RequestMapping("/web-user/update")
    Result updateWebUser(@RequestBody WebUser webUser);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/web-user/delete")
    Result deleteWebUser(@RequestParam Long id);

    /**
     * 根据条件查询用户列表
     *
     * @param webUserDto 查询条件
     * @return
     */
    @RequestMapping("/web-user/query/list")
    Result queryWebUserList(@RequestBody WebUserDto webUserDto);

    /**
     * 分配角色
     *
     * @param allocationRoleDto 用户id 角色信息
     * @return
     */
   /* @RequestMapping("/web-user/allocation/role")
    Result allocationWebUserRole(@RequestBody AllocationRoleDto allocationRoleDto);*/

}

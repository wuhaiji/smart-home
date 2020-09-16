package com.ecoeler.feign;


import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.service.IWebRoleService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * feign
 *
 * @author tang
 * @since 2020-09-10
 */


@FeignClient(name = "smart-home-service", path = "/web-role", contextId = "webRole")
public interface WebRoleFeignClient {

    /**
     * 新增角色
     *
     * @param webRole 角色信息
     * @return
     */
    @RequestMapping("save")
    Result addRole(WebRole webRole);

    /**
     * 修改角色
     *
     * @param webRole
     * @return
     */
    @RequestMapping("update")
    Result updateRole(WebRole webRole);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    Result deleteRole(Long id);

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("query/list")
    Result queryRoleList();

    /**
     * 查询不是当前用户角色列表
     *
     * @param roleId
     * @return
     */
    @RequestMapping("query/list/except/by/id")
    Result queryRoleListExceptById(Long roleId);


}

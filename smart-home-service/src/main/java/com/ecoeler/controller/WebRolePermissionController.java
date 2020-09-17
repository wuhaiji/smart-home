package com.ecoeler.controller;


import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.model.response.Result;
import com.ecoeler.app.service.IWebRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@RequestMapping("/web-role-permission")
public class WebRolePermissionController {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 定制的权限 及角色id
     * @return
     */
    @RequestMapping("customization")
    public Result customizationPermission(CustomizationPermissionDto customizationPermissionDto) {
        log.info("开始制定权限");
        System.out.println(customizationPermissionDto);
        iWebRolePermissionService.customizationPermission(customizationPermissionDto);
        return Result.ok();
    }
}

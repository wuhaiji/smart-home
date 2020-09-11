package com.ecoeler.controller;


import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.time.format.ResolverStyle;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Slf4j
@Controller
@RequestMapping("/web-permission")
public class WebPermissionController {
    @Autowired
    private IWebPermissionService iWebPermissionService;
    /***
     * 查询所有菜单权限
     * @return
     */
    public Result queryAllMenuPermission(){
        log.info("开始查询所有菜单权限");
        List<MenuWebPermissionBean> result=iWebPermissionService.selectAllMenuPermission();
        return Result.ok(result);
    }

}

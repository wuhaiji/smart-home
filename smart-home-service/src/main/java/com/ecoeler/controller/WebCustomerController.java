package com.ecoeler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyDeviceBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyMemberBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyRoomBean;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.service.IWebCustomerService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * web客户控制器
 *
 * @author tangCX
 * @since 2020/9/15
 */
@Slf4j
@RequestMapping("/web_customer")
@RestController
public class WebCustomerController {
    @Autowired
    private IWebCustomerService iWebCustomerService;

    /**
     * 分页按条件查询家庭列表
     * @param webCustomerDto 条件
     * @param page 分页
     * @return
     */
    @RequestMapping("/query/family")
    public Result queryFamily(WebCustomerDto webCustomerDto, Page<Family> page) {
        log.info("开始查询客户列表");
        PageBean<Family> result = iWebCustomerService.selectFamily(webCustomerDto,page);
        return Result.ok(result);
    }

    /**
     * 根据家庭id查询家庭成员
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/member")
    public Result queryFamilyMember(Long id) {
        log.info("开始查询客户列表");
        List<WebCustomerFamilyMemberBean> result = iWebCustomerService.selectFamilyMember(id);
        return Result.ok(result);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/room")
    public Result queryFamilyRoom(Long id) {
        log.info("开始查询客户列表");
        List<WebCustomerFamilyRoomBean> result = iWebCustomerService.selectFamilyRoom(id);
        return Result.ok(result);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/device")
    public Result queryFamilyDevice(Long id) {
        log.info("开始查询客户列表");
        List<WebCustomerFamilyDeviceBean> result = iWebCustomerService.selectFamilyDevice(id);
        return Result.ok(result);
    }

}

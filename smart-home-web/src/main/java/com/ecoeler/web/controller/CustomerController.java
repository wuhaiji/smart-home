package com.ecoeler.web.controller;

import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyDeviceBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyMemberBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyRoomBean;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.feign.WebCustomerService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色端口
 *
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/customer")
public class CustomerController {

    @Autowired
    private WebCustomerService webCustomerService;

    /**
     * 分页按条件查询家庭列表
     * @param webCustomerDto 条件
     * @return
     */
    @PreAuthorize("hasAuthority('CustomerFamily')")
    @RequestMapping("/query/family/list")
    public Result queryFamily(WebCustomerDto webCustomerDto) {
        log.info("smart-home-web->CustomerController->begin query family list");
        return webCustomerService.queryFamily(webCustomerDto);
    }

    /**
     * 根据家庭id查询家庭成员
     * @param id 家庭 id
     * @return
     */
    @PreAuthorize("hasAuthority('cus:Member')")
    @RequestMapping("/query/member")
    public Result queryFamilyMember( Long id) {
        log.info("smart-home-web->CustomerController->begin query family member");

        return webCustomerService.queryFamilyMember(id);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @PreAuthorize("hasAuthority('cus:Room')")
    @RequestMapping("/query/room")
    public Result queryFamilyRoom(Long id) {
        log.info("smart-home-web->CustomerController->begin query family room");
        return webCustomerService.queryFamilyRoom(id);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @PreAuthorize("hasAuthority('cus:AllDevice')")
    @RequestMapping("/query/device")
    public Result queryFamilyDevice(Long id) {
        log.info("smart-home-web->CustomerController->begin query family device");
        return webCustomerService.queryFamilyDevice(id);
    }
}

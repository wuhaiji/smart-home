package com.ecoeler.controller;

import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyDeviceBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyMemberBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyRoomBean;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.service.IWebCustomerService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @return
     */
    @RequestMapping("/query/family/list")
    public Result queryFamily(@RequestBody WebCustomerDto webCustomerDto) {
        log.info("smart-home-service->WebCustomerController->begin query family list");
        PageBean<Family> result = iWebCustomerService.selectFamilyList(webCustomerDto);
        return Result.ok(result);
    }

    /**
     * 根据家庭id查询家庭成员
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/member")
    public Result queryFamilyMember(@RequestParam Long id) {
        log.info("smart-home-service->WebCustomerController->begin query family member");
        List<WebCustomerFamilyMemberBean> result = iWebCustomerService.selectFamilyMember(id);
        return Result.ok(result);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/room")
    public Result queryFamilyRoom(@RequestParam Long id) {
        log.info("smart-home-service->WebCustomerController->begin query family room");
        List<WebCustomerFamilyRoomBean> result = iWebCustomerService.selectFamilyRoom(id);
        return Result.ok(result);
    }
    /**
     * 根据家庭id查询家庭房间
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/device")
    public Result queryFamilyDevice(@RequestParam Long id) {
        log.info("smart-home-service->WebCustomerController->begin query family device");
        List<WebCustomerFamilyDeviceBean> result = iWebCustomerService.selectFamilyDevice(id);
        return Result.ok(result);
    }

}

package com.ecoeler.feign;

import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * webCustomer服务
 *
 * @author tangcx
 * @since 2020/9/18
 */
@FeignClient(value = "smart-home-service", path = "/web_customer", contextId = "customer")
public interface WebCustomerService {
    /**
     * 分页按条件查询家庭列表
     *
     * @param webCustomerDto 条件
     * @return
     */
    @RequestMapping("/query/family/list")
    Result queryFamily(@RequestBody WebCustomerDto webCustomerDto);

    /**
     * 根据家庭id查询家庭成员
     *
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/member")
    Result queryFamilyMember(@RequestParam Long id);

    /**
     * 根据家庭id查询家庭房间
     *
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/room")
    Result queryFamilyRoom(@RequestParam Long id);

    /**
     * 根据家庭id查询家庭房间
     *
     * @param id 家庭 id
     * @return
     */
    @RequestMapping("/query/device")
    Result queryFamilyDevice(@RequestParam Long id);


}

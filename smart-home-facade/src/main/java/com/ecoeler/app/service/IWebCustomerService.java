package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyDeviceBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyMemberBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyRoomBean;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.entity.Family;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tangCX
 * @since 2020-09-15
 */
public interface IWebCustomerService extends IService<Family> {
    /**
     * 分页按条件查询家庭列表
     *
     * @param webCustomerDto 查询条件
     * @param page           分页
     * @return 家庭列表
     */
    PageBean<Family> selectFamily(WebCustomerDto webCustomerDto, Page<Family> page);

    /**
     * 查询当前家庭的家庭成员信息
     *
     * @param id 当前家庭id
     * @return 成员列表
     */
    List<WebCustomerFamilyMemberBean> selectFamilyMember(Long id);

    /**
     * 查询当前家庭的家庭房间信息
     *
     * @param id 当前家庭id
     * @return 房间列表
     */
    List<WebCustomerFamilyRoomBean> selectFamilyRoom(Long id);

    /**
     * 查询当前家庭的家庭设备信息
     *
     * @param id 当前家庭id
     * @return 设备列表
     */
    List<WebCustomerFamilyDeviceBean> selectFamilyDevice(Long id);
}

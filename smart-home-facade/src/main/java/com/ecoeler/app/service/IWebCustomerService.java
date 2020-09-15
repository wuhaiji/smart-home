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
 *  服务类
 * </p>
 *
 * @author tangCX
 * @since 2020-09-15
 */
public interface IWebCustomerService extends IService<Family> {

    PageBean<Family> selectFamily(WebCustomerDto webCustomerDto, Page<Family> page);

    List<WebCustomerFamilyMemberBean> selectFamilyMember(Long id);

    List<WebCustomerFamilyRoomBean> selectFamilyRoom(Long id);

    List<WebCustomerFamilyDeviceBean> selectFamilyDevice(Long id);
}

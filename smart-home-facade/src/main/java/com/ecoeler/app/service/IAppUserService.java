package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.UserDto;
import com.ecoeler.app.entity.AppUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IAppUserService extends IService<AppUser> {
    /**
     * 根据用户ID查询用户的设备列表
     * @param userDto
     * @return
     */
    List<DeviceVoiceBean> getDeviceVoiceBeans(UserDto userDto);

    /**
     * 新增用户
     * @param user
     * @return
     */
    Long createUser(AppUser user);

}

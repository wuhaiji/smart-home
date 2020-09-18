package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
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
     * 新增用户，需要给用户新增家庭
     * @param user
     * @return
     */
    Long createUser(AppUser user);

}

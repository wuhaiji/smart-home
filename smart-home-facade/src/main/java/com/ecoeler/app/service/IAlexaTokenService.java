package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.AlexaToken;

/**
 * <p>
 * 存储alexa Statereport 和changeReport要用的amazon下发的oauth2令牌信息 服务类
 * </p>
 *
 * @author whj
 * @since 2020-09-22
 */
public interface IAlexaTokenService extends IService<AlexaToken> {
    /**
     * 更新或者保存
     * @param alexaToken AlexaToken
     * @return AlexaToken
     */
    AlexaToken saveUpdate(AlexaToken alexaToken);

}

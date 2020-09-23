package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.AlexaToken;
import com.ecoeler.app.mapper.AlexaTokenMapper;
import com.ecoeler.app.service.IAlexaTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 存储alexa Statereport 和changeReport要用的amazon下发的oauth2令牌信息 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-09-22
 */
@Service
public class AlexaTokenServiceImpl extends ServiceImpl<AlexaTokenMapper, AlexaToken> implements IAlexaTokenService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlexaToken saveUpdate(AlexaToken alexaToken) {
        if (alexaToken.getId() == null) {
            //可能该用户曾经连接过yoti技能，判断是否存在该用户的token信息，如果存在着更新token信息，不存在就新增
            QueryWrapper<AlexaToken> alexaTokenQueryWrapper = new QueryWrapper<>();
            alexaTokenQueryWrapper.eq("user_id", alexaToken.getUserId());
            AlexaToken alexaToken1 = baseMapper.selectOne(alexaTokenQueryWrapper);
            if (alexaToken1 == null) {
                baseMapper.insert(alexaToken);
            } else {
                alexaToken.setId(alexaToken1.getId());
                alexaToken.setUserId(alexaToken1.getUserId());
                baseMapper.updateById(alexaToken);
            }
            return alexaToken;
        }
        //如果id不为空，这说明执行更新
        baseMapper.updateById(alexaToken);
        return alexaToken;
    }
}

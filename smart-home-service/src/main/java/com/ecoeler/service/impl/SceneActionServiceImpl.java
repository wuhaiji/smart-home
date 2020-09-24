package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.DeviceKey;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.app.mapper.DeviceKeyMapper;
import com.ecoeler.app.mapper.DeviceTypeMapper;
import com.ecoeler.app.mapper.SceneActionMapper;
import com.ecoeler.app.mapper.SceneMapper;
import com.ecoeler.app.service.ISceneActionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-21
 */
@Service
public class SceneActionServiceImpl extends ServiceImpl<SceneActionMapper, SceneAction> implements ISceneActionService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private DeviceKeyMapper deviceKeyMapper;

    @Override
    public Long addSceneAction(SceneAction sceneAction) {

        if(StringUtils.isBlank(sceneAction.getKeyInfo())) {
            QueryWrapper<DeviceKey> q = new QueryWrapper<>();
            q.eq("product_id", sceneAction.getProductId())
                    .eq("data_key", sceneAction.getDataKey());
            DeviceKey deviceKey = deviceKeyMapper.selectOne(q);
            sceneAction.setKeyInfo(deviceKey.getKeyInfo());
            sceneAction.setKeyType(deviceKey.getKeyType());
        }

        if(StringUtils.isBlank(sceneAction.getEventClass())) {
            QueryWrapper<DeviceType> q = new QueryWrapper<>();
            q.eq("product_id", sceneAction.getProductId());
            sceneAction.setEventClass(deviceTypeMapper.selectOne(q).getEventClass());
        }

        baseMapper.insert(sceneAction);

        return sceneAction.getId();
    }
}

package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.Family;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IFamilyService extends IService<Family> {
    /**
     * 列出用户家庭
     * @param userId
     * @return
     */
    List<Family> listUserFamily(Long userId);

    /**
     * 添加家庭
     * @param family
     * @param userId
     * @param nickname
     * @return
     */
    Long addFamily(Family family,Long userId,String nickname);

    Boolean removeFamily(Long id);
}

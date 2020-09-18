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

    List<Family> listUserFamily(Long userId);

}

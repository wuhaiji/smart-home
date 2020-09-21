package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.Floor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IFloorService extends IService<Floor> {

    List<Floor> listFamilyFloor(Long familyId);

    Long addFloor(Floor floor);

}

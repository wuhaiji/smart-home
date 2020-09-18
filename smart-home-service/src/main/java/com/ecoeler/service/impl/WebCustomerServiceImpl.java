package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyDeviceBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyMemberBean;
import com.ecoeler.app.bean.v1.WebCustomerFamilyRoomBean;
import com.ecoeler.app.dto.v1.WebCustomerDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.service.*;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tangCX
 * @since 2020-09-15
 */
@Service
public class WebCustomerServiceImpl extends ServiceImpl<FamilyMapper, Family> implements IWebCustomerService {
    @Autowired
    private UserFamilyMapper userFamilyMapper;
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private FloorMapper floorMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 分页查询家庭
     *
     * @param webCustomerDto
     * @param page
     * @return
     */
    @Override
    public PageBean<Family> selectFamily(WebCustomerDto webCustomerDto, Page<Family> page) {
        QueryWrapper<Family> queryWrapper = new QueryWrapper<>();
        String familyName = Optional.ofNullable(webCustomerDto.getFamilyName()).orElse("");
        String positionName = Optional.ofNullable(webCustomerDto.getPositionName()).orElse("");
        //获取查询时间
        Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webCustomerDto);
        LocalDateTime startTime = stringLocalDateTimeMap.get(TimeUtil.START);
        LocalDateTime endTime = stringLocalDateTimeMap.get(TimeUtil.END);
        //查询条件
        queryWrapper.eq(!"".equals(familyName.trim()), "family_name", familyName);
        queryWrapper.eq(!"".equals(positionName.trim()), "position_name", positionName);
        queryWrapper.ge(startTime != null, "create_time", startTime);
        queryWrapper.le(endTime != null, "create_time", endTime);
        Page<Family> familyPage = baseMapper.selectPage(page, queryWrapper);
        PageBean<Family> result = new PageBean<>();
        result.setTotal(familyPage.getTotal());
        result.setPages(familyPage.getPages());
        result.setList(familyPage.getRecords());
        return result;
    }

    /**
     * 查询家庭成员
     *
     * @param id
     * @return
     */
    @Override
    public List<WebCustomerFamilyMemberBean> selectFamilyMember(Long id) {
        List<WebCustomerFamilyMemberBean> result = new ArrayList<>();
        QueryWrapper<UserFamily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "app_user_id", "nick_name", "family_id", "role");
        queryWrapper.eq("family_id", id);
        List<UserFamily> families = userFamilyMapper.selectList(queryWrapper);
        if (families != null && families.size() != 0) {
            for (UserFamily family : families) {
                WebCustomerFamilyMemberBean bean = new WebCustomerFamilyMemberBean();
                bean.setNickName(family.getNickName());
                bean.setRole(family.getRole());
                AppUser byId = appUserMapper.selectById(family.getAppUserId());
                if (byId != null) {
                    bean.setEmail(byId.getEmail());
                    bean.setUsername(byId.getUsername());
                }
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * 查询家庭房间
     *
     * @param id 家庭 id
     * @return
     */
    @Override
    public List<WebCustomerFamilyRoomBean> selectFamilyRoom(Long id) {
        List<WebCustomerFamilyRoomBean> result = new ArrayList<>();
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "room_name", "floor_id", "family_type");
        queryWrapper.eq("family_id", id);
        List<Room> list = roomMapper.selectList(queryWrapper);
        if (list != null && list.size() != 0) {
            for (Room room : list) {
                WebCustomerFamilyRoomBean bean = new WebCustomerFamilyRoomBean();
                bean.setRoomName(room.getRoomName());
                if (room.getFloorId() != null) {
                    Floor byId = floorMapper.selectById(room.getFloorId());
                    if (byId != null) {
                        bean.setFloorName(byId.getFloorName());
                        bean.setFloorId(room.getFloorId());
                    }
                }
                result.add(bean);
            }
        }
        return result;
    }

    @Override
    public List<WebCustomerFamilyDeviceBean> selectFamilyDevice(Long id) {
        List<WebCustomerFamilyDeviceBean> result = new ArrayList<>();
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("family_id", id);
        List<Device> list = deviceMapper.selectList(queryWrapper);
        if (list != null && list.size() != 0) {
            for (Device device : list) {
                WebCustomerFamilyDeviceBean bean = new WebCustomerFamilyDeviceBean();
                BeanUtils.copyProperties(device, bean);
                //封装位置
                if (device.getRoomId() != 0) {
                    String location = "";
                    Room room = roomMapper.selectById(device.getRoomId());
                    //有楼层
                    if (room.getFloorId() != null && room.getFloorId() != 0L) {
                        Floor floor = floorMapper.selectById(room.getFloorId());
                        location = floor.getFloorName() + room.getRoomName();
                    } else {
                        location = room.getRoomName();
                    }
                    bean.setLocation(location);
                }
                result.add(bean);
            }
        }
        return result;

    }
}

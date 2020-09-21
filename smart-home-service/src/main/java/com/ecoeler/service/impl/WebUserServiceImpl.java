package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.ExceptionUtil;
import com.ecoeler.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebUserServiceImpl extends ServiceImpl<WebUserMapper, WebUser> implements IWebUserService {
    @Autowired
    private WebUserMapper webUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 查询角色对应用户的个数
     *
     * @return
     */
    @Override
    public List<WebRoleBean> selectUserCountByRoleId() {
        return webUserMapper.selectUserCountByRoleId();
    }

    /**
     * 新增用户
     *
     * @param webUser
     * @return
     */
    @Override
    public Long addWebUser(WebUser webUser) {
        log.error(webUser.toString());
        ExceptionUtil.notBlank(webUser.getUserName(), TangCode.CODE_USERNAME_EMPTY_ERROR);
        ExceptionUtil.notBlank(webUser.getPassword(), TangCode.CODE_PASSWORD_EMPTY_ERROR);
        if (webUser.getEmail() != null) {
            ExceptionUtil.notMatch(webUser.getEmail(), "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", TangCode.EMAIL_NOT_MATCH_ERROR);
        }
        ExceptionUtil.notBlank(webUser.getPhoneNumber(), TangCode.BLANK_PHONE_NUMBER_EMPTY_ERROR);
        ExceptionUtil.notInRange(webUser.getPassword(), 6, 16, TangCode.PASSWORD_NOT_IN_RANGE_ERROR);
        //校验用户是否存在
        verifyWebUserExit(webUser);
        webUser.setCreateTime(LocalDateTime.now());
        webUser.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(webUser);
        return webUser.getId();
    }

    /**
     * 校验用户是否存在以及给密码加密
     *
     * @param webUser
     */
    private void verifyWebUserExit(WebUser webUser) {
        String userName = Optional.ofNullable(webUser.getUserName()).orElse("");
        String email = Optional.ofNullable(webUser.getEmail()).orElse("");
        String phoneNumber = Optional.ofNullable(webUser.getPhoneNumber()).orElse("");
        String password = Optional.ofNullable(webUser.getPassword()).orElse("");
        if (!"".equals(password.trim())) {
            password = passwordEncoder.encode(webUser.getPassword());
            webUser.setPassword(password);
        }
        QueryWrapper<WebUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper
                .ne(webUser.getId() != null && !"".equals(email.trim()), "id", webUser.getId())
                .eq(!"".equals(email.trim()), "email", webUser.getEmail())
                .or()
                .ne(webUser.getId() != null && !"".equals(userName.trim()), "id", webUser.getId())
                .eq(!"".equals(userName.trim()), "user_name", webUser.getUserName())
                .or()
                .ne(webUser.getId() != null && !"".equals(phoneNumber.trim()), "id", webUser.getId())
                .eq(!"".equals(phoneNumber.trim()), "phone_number", webUser.getPhoneNumber());
        List<WebUser> webUsers = baseMapper.selectList(queryWrapper);
        if (webUsers.size() != 0) {
            throw new ServiceException(TangCode.CODE_USER_EXIST);
        }
    }

    /**
     * 修改用户
     *
     * @param webUser
     */
    @Override
    public void updateWebUser(WebUser webUser) {
        webUser.setUpdateTime(LocalDateTime.now());
        verifyWebUserExit(webUser);
        baseMapper.updateById(webUser);
    }

    @Override
    public void deleteWebUser(Long id) {
        baseMapper.deleteById(id);
    }

    /***
     *根据条件分页查询
     * @param webUserDto
     * @return
     */
    @Override
    public PageBean<WebUser> queryWebUserList(WebUserDto webUserDto) {
        String userName = Optional.ofNullable(webUserDto.getUserName()).orElse("");
        String email = Optional.ofNullable(webUserDto.getEmail()).orElse("");
        String phoneNo = Optional.ofNullable(webUserDto.getPhoneNumber()).orElse("");
        //默认是创建时间
        Integer timeType = Optional.ofNullable(webUserDto.getTimeType()).orElse(0);
        Page<WebUser> page = new Page<>();
        page.setSize(webUserDto.getSize());
        page.setCurrent(webUserDto.getCurrent());
        //获取查询时间
        Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webUserDto);
        LocalDateTime startTime = stringLocalDateTimeMap.get(TimeUtil.START);
        LocalDateTime endTime = stringLocalDateTimeMap.get(TimeUtil.END);
        //0-->create_time  1-->update_time
        String timeLine;
        switch (timeType) {
            case 0:
                timeLine = "create_time";
                break;
            case 1:
                timeLine = "update_time";
                break;
            default:
                timeLine = "";
                break;
        }
        QueryWrapper<WebUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "user_name", "email", "update_time", "create_time", "phone_number", "role_id", "description");
        queryWrapper.eq(!"".equals(userName.trim()), "user_name", userName);
        queryWrapper.eq(!"".equals(email.trim()), "email", email);
        queryWrapper.eq(!"".equals(phoneNo.trim()), "phone_number", phoneNo);
        queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime);
        queryWrapper.le(timeType != -1 && endTime != null, timeLine, endTime);
        //  queryWrapper.orderByDesc("id");
        Page<WebUser> webUserPage = baseMapper.selectPage(page, queryWrapper);
        PageBean<WebUser> result = new PageBean<>();
        result.setTotal(webUserPage.getTotal());
        result.setPages(webUserPage.getPages());
        result.setList(webUserPage.getRecords());
        return result;
    }

    /**
     * 给指定用户分配角色
     *
     * @param allocationRoleDto  用户 角色信息
     */
  /*  @Override
    public void allocationWebUserRole(AllocationRoleDto allocationRoleDto) {
        ExceptionUtil.notNull(allocationRoleDto.getId(), TangCode.NULL_ROLE_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(allocationRoleDto.getRole(), TangCode.NULL_ROLE_EMPTY_ERROR);
        WebUser webUser = new WebUser();
        webUser.setId(allocationRoleDto.getUserId());
        webUser.setRoleId(allocationRoleDto.getId());
        webUser.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(webUser);
    }*/


}

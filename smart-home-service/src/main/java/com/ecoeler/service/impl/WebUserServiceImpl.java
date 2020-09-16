package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.WebUserCode;
import com.ecoeler.util.ExceptionUtil;
import com.ecoeler.util.TimeUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    @Autowired
    private IWebPermissionService iWebPermissionService;

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
        ExceptionUtil.notBlank(webUser.getUserName(), WebUserCode.BLANK_USER_NAME);
        ExceptionUtil.notBlank(webUser.getPassword(), WebUserCode.BLANK_PASSWORD);
        ExceptionUtil.notBlank(webUser.getEmail(), WebUserCode.BLANK_EMAIL);
        ExceptionUtil.notBlank(webUser.getPhoneNumber(), WebUserCode.BLANK_PHONE_NUMBER);
        ExceptionUtil.notInRange(webUser.getPassword(), 6, 16, WebUserCode.PASSWORD_NOT_IN_RANGE);
        ExceptionUtil.notMatch(webUser.getEmail(), "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", WebUserCode.EMAIL_NOT_MATCH);
        try {
            String password = passwordEncoder.encode(webUser.getPassword());
            webUser.setPassword(password);
            baseMapper.insert(webUser);
            return webUser.getId();
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebUserCode.ADD);
        }
    }

    /**
     * 修改用户
     *
     * @param webUser
     */
    @Override
    public void updateWebUser(WebUser webUser) {
        try {
            baseMapper.updateById(webUser);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebUserCode.UPDATE);
        }
    }

    @Override
    public void deleteWebUser(Long id) {
        try {
            baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebUserCode.DELETE);
        }
    }

    /***
     *根据条件分页查询
     * @param webUserDto
     * @param page
     * @return
     */
    @Override
    public PageBean<WebUser> queryWebUserList(WebUserDto webUserDto, Page<WebUser> page) {
        try {
            String userName = Optional.ofNullable(webUserDto.getUserName()).orElse("");
            String email = Optional.ofNullable(webUserDto.getEmail()).orElse("");
            String phoneNo = Optional.ofNullable(webUserDto.getPhoneNumber()).orElse("");
            Integer timeType = Optional.ofNullable(webUserDto.getTimeType()).orElse(-1);
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
            //获取查询时间
            Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webUserDto);
            LocalDateTime startTime = stringLocalDateTimeMap.get(TimeUtil.START);
            LocalDateTime endTime = stringLocalDateTimeMap.get(TimeUtil.END);
            QueryWrapper<WebUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "user_name", "email", "update_time", "create_time", "phone_number", "role_id", "role");
            queryWrapper.eq(!"".equals(userName.trim()), "user_name", userName);
            queryWrapper.eq(!"".equals(email.trim()), "email", email);
            queryWrapper.eq(!"".equals(phoneNo.trim()), "phone_number", phoneNo);
            queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime);
            queryWrapper.le(timeType != -1 && endTime != null, timeLine, endTime);
            Page<WebUser> webUserPage = baseMapper.selectPage(page, queryWrapper);
            PageBean<WebUser> result = new PageBean<>();
            result.setTotal(webUserPage.getTotal());
            result.setPages(webUserPage.getPages());
            result.setList(webUserPage.getRecords());
            return result;
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebUserCode.SELECT);
        }
    }

    /**
     * 给指定用户分配角色
     *
     * @param userId  用户
     * @param webRole 角色
     */
    @Override
    public void allocationWebUserRole(Long userId, WebRole webRole) {
        ExceptionUtil.notNull(webRole.getId(), WebUserCode.NULL_ROLE_ID);
        ExceptionUtil.notNull(webRole.getRole(), WebUserCode.NULL_ROLE);
        try {
            WebUser webUser = new WebUser();
            webUser.setId(userId);
            webUser.setRoleId(webRole.getId());
            webUser.setRole(webRole.getRole());
            baseMapper.updateById(webUser);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebUserCode.ALLOCATION);
        }

    }

    /**
     * 根据用户id查询权限
     * @param userId  指定用户id
     * @return 权限
     */
    @Override
    public Set<String> getPerByUserId(Long userId) {
        return iWebPermissionService.selectBackPermissionByRoleId(baseMapper.selectById(userId).getRoleId());
    }
}

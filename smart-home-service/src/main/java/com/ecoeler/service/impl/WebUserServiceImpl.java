package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.bean.v1.WebUserBean;
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
import java.util.*;

/**
 * <p>
 * 用户实现服务类
 * </p>
 *
 * @author tangcx
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
     * @param webUser 用户信息
     * @return 新增用户id
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
        //webUser.setCreateTime(LocalDateTime.now());
       // webUser.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(webUser);
        return webUser.getId();
    }

    /**
     * 校验用户是否存在以及给密码加密
     *
     * @param webUser 用户信息
     */
    private void verifyWebUserExit(WebUser webUser) {
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
     * @param webUser 用户
     */
    @Override
    public void updateWebUser(WebUser webUser) {
       // webUser.setUpdateTime(LocalDateTime.now());
        verifyWebUserExit(webUser);
        baseMapper.updateById(webUser);
    }

    /**
     * 删除用户
     * @param id 用户Id
     */
    @Override
    public void deleteWebUser(Long id) {
        baseMapper.deleteById(id);
    }

    /***
     *根据条件分页查询
     * @param webUserDto 查询条件
     * @return 分页的用户信息
     */
    @Override
    public PageBean<WebUserBean> queryWebUserList(WebUserDto webUserDto) {
        String userName = Optional.ofNullable(webUserDto.getUserName()).orElse("");
        String email = Optional.ofNullable(webUserDto.getEmail()).orElse("");
        String phoneNo = Optional.ofNullable(webUserDto.getPhoneNumber()).orElse("");
        Long roleId=Optional.ofNullable(webUserDto.getRoleId()).orElse(-1L);
        if ("".equals(userName.trim())) {
            userName = null;
        }
        if ("".equals(email.trim())) {
            email = null;
        }
        if ("".equals(phoneNo.trim())) {
            phoneNo = null;
        }
        //默认是创建时间
        Integer timeType = Optional.ofNullable(webUserDto.getTimeType()).orElse(0);
        Page<WebUser> page = new Page<>();
        page.setSize(webUserDto.getSize());
        page.setCurrent(webUserDto.getCurrent());
        //获取查询时间
        Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webUserDto);
        Date startTime = webUserDto.getStartTime();
        Date endTime = webUserDto.getEndTime();
        //0-->create_time  1-->update_time
        String timeLine;
        if (timeType == 1) {
            timeLine = "update_time";
        } else {
            timeLine = "create_time";
        }
        webUserDto.setEmail(email);
        webUserDto.setPhoneNumber(phoneNo);
        webUserDto.setTimeLine(timeLine);
        webUserDto.setTimeType(timeType);
        webUserDto.setUserName(userName);
        webUserDto.setRoleId(roleId);
        webUserDto.setLimitStart((webUserDto.getCurrent()-1)*webUserDto.getSize());
        //查询总数
        QueryWrapper<WebUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userName!=null, "user_name", userName);
        queryWrapper.eq(email!=null, "email", email);
        queryWrapper.eq(phoneNo!=null, "phone_number", phoneNo);
        queryWrapper.eq(roleId!=-1,"role_id",roleId);
        queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime);
        queryWrapper.le(timeType != -1 && endTime != null, timeLine, endTime);
        Integer total = baseMapper.selectCount(queryWrapper);
        //查询列表
        List<WebUserBean> userResult=new ArrayList<>();
        if (total>0){
           userResult=webUserMapper.selectUserList(webUserDto);
        }
        PageBean<WebUserBean> result = new PageBean<>();
        result.setTotal(Long.parseLong(total.toString()));
        int pages=total/webUserDto.getSize();
        if (total%webUserDto.getSize()!=0){
            pages++;
        }
        result.setPages(Long.parseLong(Integer.toString(pages)));
        result.setList(userResult);
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

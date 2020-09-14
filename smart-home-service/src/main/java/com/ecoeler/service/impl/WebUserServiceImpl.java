package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.WebUserDto;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.WebUserCode;
import com.ecoeler.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
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
     * @return
     */
    @Override
    public List<WebRoleBean>  selectUserCountByRoleId() {
        return webUserMapper.selectUserCountByRoleId();
    }

    /**
     * 新增用户
     * @param webUser
     * @return
     */
    @Override
    public Long addWebUser(WebUser webUser) {
//        ErrorUtils.isStringEmpty(webUser.getUserName(),"用户名");
//        ErrorUtils.isStringEmpty(webUser.getPassword(),"密码");
//        ErrorUtils.isNumberValueIn(webUser.getPassword().length(),6,16,"密码长度");
//        ErrorUtils.isStringEmpty(webUser.getEmail(),"邮箱");
//        ErrorUtils.isStringMatch(webUser.getEmail(),"^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$","邮箱格式");
//        ErrorUtils.isNullable(webUser.getPhoneNumber(),"手机号");

        try {
            String password=passwordEncoder.encode(webUser.getPassword());
            webUser.setPassword(password);
            baseMapper.insert(webUser);
            return webUser.getId();
        }catch (Exception e){
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw  new ServiceException(WebUserCode.ADD);
        }
    }

    /**
     * 修改用户
     * @param webUser
     */
    @Override
    public void updateWebUser(WebUser webUser) {
        //ErrorUtils.isNullable(webUser.getId(),"用户Id");
        try {
            baseMapper.updateById(webUser);
        }catch (Exception e){
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw  new ServiceException(WebUserCode.UPDATE);
        }
    }

    @Override
    public void deleteWebUser(Long id) {
        try {
            baseMapper.deleteById(id);
        }catch (Exception e){
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw  new ServiceException(WebUserCode.DELETE);
        }
    }

    @Override
    public PageBean<WebUser> queryWebUserList(WebUserDto webUserDto, Page<WebUser> page) {
        String userName=Optional.ofNullable(webUserDto.getUserName()).orElse("");
        String email=Optional.ofNullable(webUserDto.getEmail()).orElse("");
        String phoneNo=Optional.ofNullable(webUserDto.getPhoneNumber()).orElse("");
        Integer timeType=Optional.ofNullable(webUserDto.getTimeType()).orElse(-1);
        //0-->create_time  1-->update_time
        String timeLine;
        switch (timeType){
            case 0:timeLine="create_time";break;
            case 1:timeLine="update_time";break;
            default:timeLine="";break;
        }
        String start=Optional.ofNullable(webUserDto.getStartTime()).orElse("");
        String end=Optional.ofNullable(webUserDto.getEndTime()).orElse("");
        LocalDateTime startTime=null;
        LocalDateTime endTime=null;
        if (!"".equals(start)){
            startTime= TimeUtil.timeFormat(start);
        }
        if (!"".equals(end)){
            endTime= TimeUtil.timeFormat(end);
        }
        if (startTime!=null&&endTime!=null){
            //ErrorUtils.isTimeBefore(startTime,endTime,"");
        }
        QueryWrapper<WebUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","user_name","email","update_time","create_time","phone_number","role_id","role");
        queryWrapper.eq(!"".equals(userName.trim()),"user_name",userName);
        queryWrapper.eq(!"".equals(email.trim()),"email",email);
        queryWrapper.eq(!"".equals(phoneNo.trim()),"phone_number",phoneNo);
        queryWrapper.ge(timeType!=-1&&startTime!=null,timeLine,startTime);
        queryWrapper.le(timeType!=-1&&endTime!=null,timeLine,endTime);
        Page<WebUser> webUserPage = baseMapper.selectPage(page, queryWrapper);
        PageBean<WebUser> result=new PageBean<>();
        result.setTotal(webUserPage.getTotal());
        result.setPages(webUserPage.getPages());
        result.setList(webUserPage.getRecords());
        return result;

    }
}

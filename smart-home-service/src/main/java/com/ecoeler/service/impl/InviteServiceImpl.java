package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.InviteRecord;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.mapper.InviteRecordMapper;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.app.service.IInviteService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.InviteCode;
import com.ecoeler.utils.AliMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author wujihong
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteRecordMapper, InviteRecord> implements IInviteService {

    @Autowired
    private InviteRecordMapper inviteRecordMapper;
    
    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private UserFamilyMapper userFamilyMapper;

    @Autowired
    private AliMailUtil aliMailUtil;

    public static Logger logger = LoggerFactory.getLogger(InviteServiceImpl.class);

    @Override
    public Boolean sendInvite(InviteRecordDto inviteRecordDto) {
        Integer result;
        // 保证邀请人的邮箱已经注册
        // 通过邮箱去查询用户id是否存在
        getAppUserInfo(inviteRecordDto.getReceiverEmail());

        // 查询邀请信息
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();
        inviteRecordQueryWrapper.eq(inviteRecordDto.getInviterEmail() != null,"inviter_email", inviteRecordDto.getInviterEmail());
        inviteRecordQueryWrapper.eq(inviteRecordDto.getReceiverEmail() != null,"receiver_email", inviteRecordDto.getReceiverEmail());
        inviteRecordQueryWrapper.eq(inviteRecordDto.getFamilyId() != null,"family_id", inviteRecordDto.getFamilyId());
        InviteRecord queryResult = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);
        InviteRecord inviteRecord = new InviteRecord();

        // 当不存在邀请记录时
        if (queryResult == null) {
            // 存储邀请记录
            BeanUtils.copyProperties(inviteRecordDto, inviteRecord);
            result = inviteRecordMapper.insert(inviteRecord);
            if(result <= 0) {
                throw new ServiceException(InviteCode.INSERT_INVITE_RECORD_SERVICE_ERROR);
            }
            inviteRecordDto.setId(inviteRecord.getId());
        }
        // 存在邀请记录时
        else {
            // 修改邀请记录
            inviteRecordDto.setId(queryResult.getId());
            BeanUtils.copyProperties(inviteRecordDto, inviteRecord);
            result = inviteRecordMapper.updateById(inviteRecord);
            if(result <= 0) {
                throw new ServiceException(InviteCode.UPDATE_INVITE_RECORD_SERVICE_ERROR);
            }
        }
        // 发送邀请邮件
        aliMailUtil.sendInvite(inviteRecordDto);
        return result!=null?true:false;
    }

    @Override
    public String acceptInvite(Long id) {
        String result = null;
        LocalDateTime now = LocalDateTime.now();

        // 1.判断邀请人是否已在所邀请家庭中（防止重复插入）
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();
        inviteRecordQueryWrapper.eq("id", id);
        // 查询当前邀请信息
        InviteRecord inviteRecord = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);

        if (inviteRecord != null) {
            // 2.判断邀请是否失效
            checkInviteLoseEffect(inviteRecord.getInviteTime());
            // 通过邮箱去查询用户信息
            AppUser appUser = getAppUserInfo(inviteRecord.getReceiverEmail());
            // 通过用户app_user_id、family_id去查询当前用户是否在家庭中
            QueryWrapper<UserFamily> userFamilyQueryWrapper = new QueryWrapper<>();
            userFamilyQueryWrapper.eq("app_user_id", appUser.getId());
            userFamilyQueryWrapper.eq("family_id", inviteRecord.getFamilyId());
            Integer queryResult = userFamilyMapper.selectCount(userFamilyQueryWrapper);
            // 当前用户不在家庭中
            if (queryResult <= 0) {
                UserFamily userFamily = new UserFamily();
                userFamily.setAppUserId(appUser.getId());
                userFamily.setFamilyId(inviteRecord.getFamilyId());
                userFamily.setNickName(inviteRecord.getNickName());
                userFamily.setRole(inviteRecord.getRole());
                // 将被邀请的用户加入到家庭中
                if (userFamilyMapper.insert(userFamily) > 0) {
                    // 修改邀请记录中的邀请状态和响应时间
                    inviteRecord.setStatus(1);
                    inviteRecord.setResponseTime(LocalDateTime.now());
                    inviteRecordMapper.updateById(inviteRecord);
                    logger.info("被邀请用户加入家庭成功！");
                    result = "被邀请用户加入家庭成功！";
                }
            }
            // 当前用户在家庭中
            else {
                logger.warn("警告，被邀请用户已出现在家庭中！");
                throw new ServiceException(InviteCode.EXIST_FAMILY_USER);
            }

        } else {
            throw new ServiceException(InviteCode.INVITE_LOSE_EFFECT);
        }

        return result;
    }

    @Override
    public String refuseInvite(Long id) {
        String result = null;
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();
        inviteRecordQueryWrapper.eq("id", id);
        inviteRecordQueryWrapper.select("invite_time", "status");
        InviteRecord queryInviteRecord = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);
        // 检查当前邀请是否过期
        checkInviteLoseEffect(queryInviteRecord.getInviteTime());
        // 判断当前邀请是否已经接受
        if (queryInviteRecord.getStatus() != 1) {
            InviteRecord inviteRecord = new InviteRecord();
            inviteRecord.setId(id);
            inviteRecord.setStatus(2);
            inviteRecord.setResponseTime(LocalDateTime.now());
            int update = inviteRecordMapper.updateById(inviteRecord);
            if (update > 0) {
                logger.info("该用户拒绝加入家庭！");
                result = "你已拒绝加入家庭!";
            }
        }else {
            logger.warn("警告，拒绝邀请无效（因为：该用户已加入到家庭）！");
            throw new ServiceException(InviteCode.INVALID_REFUSE_INVITE);
        }

        return result;
    }

    /**
     * 通过邮箱查询用户信息
     * @author wujihong
     * @param email
     * @since 14:52 2020-09-23
     */
    public AppUser getAppUserInfo(String email) {
        AppUser appUser;
        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.eq("email", email);
        appUser = appUserMapper.selectOne(appUserQueryWrapper);
        if (appUser == null) {
            throw new ServiceException(InviteCode.USER_UNREGISTERED_SERVICE_ERROR);
        }
        return appUser;
    }

    /**
     * 检测邀请是否失效
     * @author wujihong
     * @param before
     * @since 15:29 2020-09-23
     */
    public void checkInviteLoseEffect(LocalDateTime before) {
        // now-before
        Duration between = Duration.between(before, LocalDateTime.now());
        if (between.toDays() >= 7L) {
            logger.warn("邀请至今已有{}天了！", between.toDays());
            throw new ServiceException(InviteCode.INVITE_LOSE_EFFECT);
        }
    }
}

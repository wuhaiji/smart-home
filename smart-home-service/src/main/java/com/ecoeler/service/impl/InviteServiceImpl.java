package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.utils.AliMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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

    @Value("${invite.effective.time}")
    private String effectiveTime;

    public static Logger logger = LoggerFactory.getLogger(InviteServiceImpl.class);

    @Override
    public void sendInvite(InviteRecordDto inviteRecordDto) {
        // UTC时间
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        String receiverEmail = inviteRecordDto.getReceiverEmail();
        Long familyId = inviteRecordDto.getFamilyId();
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();

        // 保证被邀请人的邮箱已经注册
        // 1.检验被邀请人是否已注册账号
        AppUser appUserInfo = getAppUserInfo(receiverEmail);
        if(appUserInfo==null){
            throw new ServiceException(WJHCode.USER_UNREGISTERED_SERVICE_ERROR);
        }

        // 2.查询被邀请人是否已经出现在邀请家庭中
        if(userFamilyMapper.selectCount(new LambdaQueryWrapper<UserFamily>()
                .eq(UserFamily::getFamilyId,inviteRecordDto.getFamilyId())
                .eq(UserFamily::getAppUserId,appUserInfo.getId())) != 0){
            throw new ServiceException(WJHCode.USER_EXIST_FAMILY_ERROR);
        }

        // 通过inviterId->查询邀请人名字
        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.eq("id", inviteRecordDto.getInviterId());
        appUserQueryWrapper.select("username");
        inviteRecordDto.setInviterName(appUserMapper.selectOne(appUserQueryWrapper).getUsername());

        // 查询邀请记录
        inviteRecordQueryWrapper.eq(inviteRecordDto.getInviterId() != null,"inviter_id", inviteRecordDto.getInviterId());
        inviteRecordQueryWrapper.eq(inviteRecordDto.getReceiverEmail() != null,"receiver_email", inviteRecordDto.getReceiverEmail());
        inviteRecordQueryWrapper.eq(inviteRecordDto.getFamilyId() != null,"family_id", inviteRecordDto.getFamilyId());
        InviteRecord queryResult = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);
        InviteRecord inviteRecord = new InviteRecord();

        // 3.查询邀请记录是否存在
        if (queryResult == null) {
            // 存储邀请记录
            BeanUtils.copyProperties(inviteRecordDto, inviteRecord);
            if(inviteRecordMapper.insert(inviteRecord) <= 0) {
                throw new ServiceException(WJHCode.INSERT_INVITE_RECORD_SERVICE_ERROR);
            }
            inviteRecordDto.setId(inviteRecord.getId());
        } else {
            // 查看上一次邀请距现在是否已过有效期（effectiveTime）
            long interval = Duration.between(queryResult.getInviteTime(), now).toDays();
            // 什么情况下发送邮件？1.当被邀请人响应后；2.当用户未响应，间隔x（x：配置好的有效时间）天后；
            if (queryResult.getStatus() != 0 || interval >= Long.valueOf(effectiveTime)) {
                // 4.修改邀请记录-发送邮件
                inviteRecordDto.setId(queryResult.getId());
                BeanUtils.copyProperties(inviteRecordDto, inviteRecord);
                if(inviteRecordMapper.updateById(inviteRecord) <= 0) {
                    throw new ServiceException(WJHCode.UPDATE_INVITE_RECORD_SERVICE_ERROR);
                }
            } else {
                logger.info("发送邀请失败！原因：上一次的邀请还未失效！");
                throw new ServiceException(WJHCode.INVITE_NO_EFFECTIVE_ERROR);
            }
        }
        // 发送邀请邮件
        aliMailUtil.sendInvite(inviteRecordDto);
    }

    @Override
    public String acceptInvite(Long id, String inviteTime) {
        String result = null;
        LocalDateTime inviterTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();
        inviteRecordQueryWrapper.eq("id", id);
        // 查询邀请记录
        InviteRecord queryInviteRecord = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);
        // 1.检查当前邀请是否过期
        inviterTime = LocalDateTime.parse(inviteTime, formatter);
        checkInviteLoseEffect(inviterTime);

        // 2.检验被邀请用户是否已在家庭中
        UserFamily userFamily = getUserFamily(queryInviteRecord.getReceiverEmail(), queryInviteRecord.getFamilyId());
        if (userFamily == null) {
            // 3.通过被邀请人邮箱查询其用户信息
            AppUser appUserInfo = getAppUserInfo(queryInviteRecord.getReceiverEmail());

            UserFamily newUserFamily = new UserFamily();
            newUserFamily.setAppUserId(appUserInfo.getId());
            newUserFamily.setFamilyId(queryInviteRecord.getFamilyId());
            newUserFamily.setNickName(queryInviteRecord.getNickName());
            newUserFamily.setRole(queryInviteRecord.getRole());

            // 将被邀请用户加入到家庭
            if (userFamilyMapper.insert(newUserFamily) > 0) {
                queryInviteRecord.setStatus(1);
                queryInviteRecord.setResponseTime(LocalDateTime.now());
                // 修改邀请记录的状态
                if (inviteRecordMapper.updateById(queryInviteRecord) > 0) {
                    logger.info("被邀请用户加入家庭成功！");
                    result = "被邀请用户加入家庭成功！";
                }
            }
        } else {
            logger.warn("警告，被邀请用户已出现在家庭中！");
            throw new ServiceException(WJHCode.EXIST_FAMILY_USER);
        }
        return result;
    }

    @Override
    public String refuseInvite(Long id, String inviteTime) {
        String result = null;
        LocalDateTime inviterTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<InviteRecord> inviteRecordQueryWrapper = new QueryWrapper<>();
        inviteRecordQueryWrapper.eq("id", id);
        InviteRecord queryInviteRecord = inviteRecordMapper.selectOne(inviteRecordQueryWrapper);
        // 1.检查当前邀请是否过期
        inviterTime = LocalDateTime.parse(inviteTime, formatter);
        checkInviteLoseEffect(inviterTime);

        // 2.检验被邀请用户是否已在家庭中
        UserFamily userFamily = getUserFamily(queryInviteRecord.getReceiverEmail(), queryInviteRecord.getFamilyId());
        if (userFamily == null) {
            InviteRecord inviteRecord = new InviteRecord();
            inviteRecord.setId(id);
            inviteRecord.setStatus(2);
            inviteRecord.setResponseTime(LocalDateTime.now());
            int update = inviteRecordMapper.updateById(inviteRecord);
            if (update > 0) {
                logger.info("该用户拒绝加入家庭！");
                result = "你已拒绝加入家庭!";
            }
        } else {
            logger.warn("警告，拒绝邀请无效（因为：该用户已加入到家庭）！");
            throw new ServiceException(WJHCode.INVALID_REFUSE_INVITE);
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
        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.eq("email", email);
        return appUserMapper.selectOne(appUserQueryWrapper);
    }

    /**
     * 通过被邀请人邮箱、家庭id去查询用户-家庭信息
     * @author wujihong
     * @param email,familyId
     * @since 15:45 2020-09-29
     */
    public UserFamily getUserFamily(String email, Long familyId) {
        QueryWrapper<UserFamily> userFamilyQueryWrapper = new QueryWrapper<>();
        // 通过邮箱查询用户信息
        AppUser appUserInfo = getAppUserInfo(email);
        userFamilyQueryWrapper.eq("app_user_id", appUserInfo.getId());
        userFamilyQueryWrapper.eq("family_id", familyId);
        return userFamilyMapper.selectOne(userFamilyQueryWrapper);
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
        if (between.toDays() >= Long.valueOf(effectiveTime)) {
            logger.warn("邀请至今已有{}天了！", between.toDays());
            throw new ServiceException(WJHCode.INVITE_LOSE_EFFECT);
        }
    }
}

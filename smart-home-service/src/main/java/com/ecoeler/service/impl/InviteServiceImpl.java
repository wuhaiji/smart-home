package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.entity.InviteRecord;
import com.ecoeler.app.mapper.InviteRecordMapper;
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
import java.time.ZoneOffset;

/**
 * @author wujihong
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteRecordMapper, InviteRecord> implements IInviteService {

    @Autowired
    private InviteRecordMapper inviteRecordMapper;

    @Autowired
    private AliMailUtil aliMailUtil;

    public static Logger logger = LoggerFactory.getLogger(InviteServiceImpl.class);

    @Override
    public Boolean sendInvite(InviteRecordDto inviteRecordDto) {

        Integer result;
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
}

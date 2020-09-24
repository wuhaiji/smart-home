package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.entity.InviteRecord;

/**
 * @author wujihong
 */
public interface IInviteService extends IService<InviteRecord> {
    Boolean sendInvite(InviteRecordDto inviteRecordDto);

    String acceptInvite(Long id);

    String refuseInvite(Long id);
}

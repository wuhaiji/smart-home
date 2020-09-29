package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.dto.v1.InviteRecordDto;
import com.ecoeler.app.entity.InviteRecord;

/**
 * @author wujihong
 */
public interface IInviteService extends IService<InviteRecord> {
    void sendInvite(InviteRecordDto inviteRecordDto);

    String acceptInvite(Long id, String inviteTime);

    String refuseInvite(Long id, String inviteTime);
}

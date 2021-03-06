package com.ecoeler.app.dto.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wujihong
 */
@Data
public class InviteRecordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long inviterId;

    private String inviterName;

    private LocalDateTime inviteTime = LocalDateTime.now();

    private String receiverEmail;

    private String nickName;

    private Long familyId;

    private String familyName;

    private Integer role;
}

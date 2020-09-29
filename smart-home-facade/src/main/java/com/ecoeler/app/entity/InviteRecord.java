package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wujihong
 */
@Data
@Accessors(chain = true)
@TableName("sh_invite_record")
public class InviteRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long inviterId;

    private String inviterName;

    private LocalDateTime inviteTime;

    private String receiverEmail;

    private String nickName;

    private LocalDateTime responseTime;

    private Long familyId;

    private String familyName;

    private Integer status;

    private Integer role;
}

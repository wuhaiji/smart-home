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

    private String inviterEmail;

    private String inviterName;

    private String receiverEmail;

    private String nickName;

    private Long familyId;

    private String familyName;

    private LocalDateTime inviteTime;
}

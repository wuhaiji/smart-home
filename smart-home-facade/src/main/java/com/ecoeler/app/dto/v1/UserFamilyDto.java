package com.ecoeler.app.dto.v1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserFamilyDto {

    private Long id;

    private Long appUserId;

    /**
     * 任命的新家庭拥有者的id
     */
    private Long newAppUserOwnerId;

    private Long familyId;

    /**
     * 家庭类型：0别墅，1住宅
     */
    private Integer familyType;

    /**
     * 0家庭拥有者，1管理员，2普通成员
     */
    private Integer role;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}

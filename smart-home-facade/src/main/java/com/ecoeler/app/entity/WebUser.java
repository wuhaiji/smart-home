package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("sh_web_user")
public class WebUser implements Serializable {

    private static final long serialVersionUID = 123456789L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;


    /**
     * 头像
     */
    private String headImage;


    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long roleId;
    /**
     * 说明
     */
    private String description;



}

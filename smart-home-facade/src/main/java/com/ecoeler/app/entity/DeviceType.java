package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("sh_device_type")
public class DeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 类型名称
     */
    private String zhTypeName;

    /**
     * 类型名称
     */
    private String enTypeName;

    /**
     * 网关类型：0否，显示在首页，1是，不显示在首页
     */
    private Integer gatewayLike;

    /**
     * 产品ID
     */
    private String productId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    /**
     * 默认图标
     */
    private String defaultIcon;

    /**
     * 谷歌类型名称
     */
    private String googleTypeName;
    /**
     * 谷歌类型所具有的特性，逗号分隔
     */
    private String googleTraitNames;
    /**
     * 设备类型对应alexa app的展示名称
     */
    private String alexaDisplayName;

    /**
     * 执行的指令的class
     */
    private String eventClass;

    /**
     * 图片地址
     */
    private String image;
    /**
     * 存储图片生成的MD5 用于删除图片
     */
    @TableField("go_fastDFSMD5")
    private String goFastDFSMD5;

    /**
     * 一级中文名称
     */
    private String zhPrimaryType;

    /**
     * 一级英文名称
     */
    private String  enPrimaryType;

    /**
     * 配网类型
     */
    private String netType;


}

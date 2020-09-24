package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_scene_action")
public class SceneAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 延时，单位秒
     */
    private Integer delayTime;

    /**
     * 键
     */
    private String dataKey;

    /**
     * 值
     */
    private String dataValue;

    /**
     * 场景ID
     */
    private Long sceneId;


    //--------- 新增时以下字段查询插入 -----------


    /**
     * 键类型
     */
    private String keyType;

    /**
     * 键信息
     */
    private String keyInfo;

    /**
     * 事件处理类
     */
    private String eventClass;

}

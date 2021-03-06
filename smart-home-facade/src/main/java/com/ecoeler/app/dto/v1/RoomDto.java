package com.ecoeler.app.dto.v1;

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
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoomDto {

    private Long id;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 房间类型--对应房间的背景图片
     */
    private String roomType;

    /**
     * 在线设备总数
     */
    private Integer onlineDeviceNumber;

    /**
     * 设备总数
     */
    private Integer totalDeviceNumber;

    /**
     * 楼层ID，别墅家庭才有楼层
     */
    private Long floorId;


    private Long familyId;

    /**
     * true:表示删除家庭
     * false:表示不删除家庭
     */
    private Boolean removeFamilyBool;


}

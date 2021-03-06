package com.ecoeler.app.bean.v1;

import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class WebCustomerFamilyRoomBean {
    /**
     * 房间名
     */
    private String roomName;

    /**
     * 楼层ID，别墅家庭才有楼层
     */
    private Long floorId;

    /**
     * 楼层名称
     */
    private String floorName;

}

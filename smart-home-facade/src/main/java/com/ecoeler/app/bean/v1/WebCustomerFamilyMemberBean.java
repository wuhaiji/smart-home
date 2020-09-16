package com.ecoeler.app.bean.v1;

import lombok.Data;

/**
 * @author tangcx
 */
@Data
public class WebCustomerFamilyMemberBean {
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 0家庭拥有者，1管理员，2普通成员
     */
    private Integer role;

    /**
     * 昵称
     */
    private String nickName;

}

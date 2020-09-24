package com.ecoeler.app.bean.v1;

import com.ecoeler.app.entity.WebUser;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
/**
 * @author tangcx
 */
@Data
public class WebUserBean extends WebUser {
    private String roleName;
}

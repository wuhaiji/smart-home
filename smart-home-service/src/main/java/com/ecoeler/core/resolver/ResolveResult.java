package com.ecoeler.core.resolver;

import com.ecoeler.core.msg.KeyMsg;
import lombok.Data;

import java.util.List;

/**
 * 分解结果
 * @author tang
 * @since 2020/9/15
 */
@Data
public class ResolveResult {

    private String deviceId;

    private String productId;

    private List<KeyMsg> keyMsgList;

}

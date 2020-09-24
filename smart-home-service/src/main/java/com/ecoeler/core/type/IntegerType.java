package com.ecoeler.core.type;

import lombok.Data;

/**
 * 整型
 * @author tang
 * @since 2020/7/20
 */
@Data
public class IntegerType implements IType {

    /**
     * 单位
     */
    private String zhUnit;

    /**
     * 单位
     */
    private String enUnit;

    /**
     * 最小
     */
    private Integer min;

    /**
     * 最大
     */
    private Integer max;

    /**
     * 缩放，输出时 乘上，输入时 除以
     */
    private Integer scale;

    /**
     * 步长
     */
    private Integer step;

    @Override
    public Object checkout(Object value) {
        int i= Integer.parseInt(value.toString()) * (10^scale);
        if(!(i>=min && i<=max)){
            throw new IllegalArgumentException("----value doesn't match the rule!");
        }
        return i;
    }

    @Override
    public Object checkin(Object value) {
        int i= Integer.parseInt(value.toString()) / (10^scale);
        if(!(i>=min && i<=max)){
            throw new IllegalArgumentException("----value doesn't match the rule!");
        }
        return i;
    }
}

package com.ecoeler.core.type;

import lombok.Data;

import java.util.List;

/**
 * 枚举
 * @author tang
 * @since 2020/7/20
 */
@Data
public class EnumType extends NormalTypeAdapter {

    private List<String> range;
    
    @Override
    public Object match(Object value) {
        if(range.indexOf(value.toString()) < 0){
            throw new IllegalArgumentException("----value doesn't match the rule!");
        }
        return value;
    }
}

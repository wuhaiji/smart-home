package com.ecoeler.core.type;


import lombok.Data;

/**
 * 布尔类型
 * @author tang
 * @since 2020/9/24
 */
@Data
public class BooleanType extends NormalTypeAdapter {

    @Override
    public Object match(Object value) {
        if(value instanceof Integer){
            return ((Integer) value) == 1;
        }

        if(value instanceof String){
            return Boolean.parseBoolean((String) value);
        }

        return value;
    }

}

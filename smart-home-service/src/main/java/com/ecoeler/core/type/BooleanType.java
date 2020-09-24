package com.ecoeler.core.type;


import lombok.Data;

/**
 * 布尔类型
 * @author tang
 * @since 2020/9/24
 */
@Data
public class BooleanType implements IType {

    private String zhTrue;

    private String zhFalse;

    private String enTrue;

    private String enFalse;

    @Override
    public Object checkout(Object value) {
        return null;
    }

    @Override
    public Object checkin(Object value) {
        return null;
    }
}

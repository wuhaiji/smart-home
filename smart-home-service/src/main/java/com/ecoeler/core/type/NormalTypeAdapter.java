package com.ecoeler.core.type;

/**
 * 普通类型适配器
 * @author tang
 * @since 2020/7/21
 */
public class NormalTypeAdapter implements IType {

    public Object match(Object value){
        return false;
    }

    @Override
    public Object checkout(Object value) {
        return this.match(value);
    }

    @Override
    public Object checkin(Object value) {
        return this.match(value);
    }
}

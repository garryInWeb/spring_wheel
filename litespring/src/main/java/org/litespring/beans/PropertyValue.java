package org.litespring.beans;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class PropertyValue {
    private final Object value;
    private final String name;

    public PropertyValue(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

package org.litespring.core.annotation;

import org.litespring.utils.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhengtengfei on 2018/8/6.
 */
public class AnnotationAttributes extends LinkedHashMap<String,Object>{
    public AnnotationAttributes(){

    }

    public AnnotationAttributes(int initialCapacity) {
        super(initialCapacity);
    }

    public AnnotationAttributes(Map<? extends String, ?> m) {
        super(m);
    }
    public String getString(String attributeName){
        return doGet(attributeName,String.class);
    }

    public String[] getStringArray(String attributeName) {
        return doGet(attributeName, String[].class);
    }

    public boolean getBoolean(String attributeName) {
        return doGet(attributeName, Boolean.class);
    }

    @SuppressWarnings("unchecked")
    public <N extends Number> N getNumber(String attributeName) {
        return (N) doGet(attributeName, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<?>> E getEnum(String attributeName) {
        return (E) doGet(attributeName, Enum.class);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getClass(String attributeName) {
        return doGet(attributeName, Class.class);
    }

    public Class<?>[] getClassArray(String attributeName) {
        return doGet(attributeName, Class[].class);
    }

    private <T> T doGet(String attributeName, Class<T> exceptedType) {
        Object value = this.get(attributeName);
        Assert.notNull(value,String.format("Attribute %s not found.",attributeName));
        return (T) value;
    }
}

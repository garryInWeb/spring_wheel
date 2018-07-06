package org.litespring.beans;

import org.litespring.utils.ClassUtils;

import java.beans.PropertyEditor;
import java.util.Map;

/**
 * Created by zhengtengfei on 2018/7/6.
 */
public class SimpleTypeConverter implements TypeConverter {

    private Map<Class<?>,PropertyEditor> defaultEditor;

    public SimpleTypeConverter() {
    }

    @Override
    public <T> T converterIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {

        if (ClassUtils.isAssignableValue(requiredType,value)){
            return (T)value;
        }
        //TODO
        return null;
    }
}

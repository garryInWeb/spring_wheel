package org.litespring.beans;

/**
 * Created by zhengtengfei on 2018/7/6.
 */
public interface TypeConverter {
    <T> T converterIfNecessary(Object value,Class<T> requiredType) throws TypeMismatchException;
}


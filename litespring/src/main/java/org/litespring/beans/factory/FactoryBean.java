package org.litespring.beans.factory;

/**
 * Created by zhengtengfei on 2018/9/5.
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();
}

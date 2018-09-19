package org.litespring.beans.factory.config;

/**
 * Created by zhengtengfei on 2018/8/23.
 */
public interface BeanPostProcessor {
    Object afterInitialization(Object result, String beanName);
}

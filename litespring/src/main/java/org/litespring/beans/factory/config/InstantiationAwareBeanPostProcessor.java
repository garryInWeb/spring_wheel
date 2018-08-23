package org.litespring.beans.factory.config;

import org.litespring.beans.BeansException;

/**
 * Created by zhengtengfei on 2018/8/23.
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{
    void postProcessPropertyValues(Object bean, String beanName)
            throws BeansException;
}

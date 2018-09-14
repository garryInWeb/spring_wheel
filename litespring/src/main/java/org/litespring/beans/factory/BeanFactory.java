package org.litespring.beans.factory;

import org.litespring.aop.Advice;

import java.util.List;

public interface BeanFactory {
    Object getBean(String beanId);

    Class<?> getType(String targetBeanName);

    List<Object> getBeansByType(Class<?> type);
}

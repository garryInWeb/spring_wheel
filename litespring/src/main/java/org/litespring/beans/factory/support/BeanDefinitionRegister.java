package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

public interface BeanDefinitionRegister {
    BeanDefinition getBeanDefinition(String beanId);

    void registerBeanDefinition(BeanDefinition beanDefinition);
}

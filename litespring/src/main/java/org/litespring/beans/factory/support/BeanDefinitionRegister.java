package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanFactory;

public interface BeanDefinitionRegister{
    BeanDefinition getBeanDefinition(String beanId);

    void registerBeanDefinition(BeanDefinition beanDefinition);
}

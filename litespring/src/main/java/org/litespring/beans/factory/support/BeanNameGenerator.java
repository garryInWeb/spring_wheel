package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

/**
 * Created by zhengtengfei on 2018/8/9.
 */
public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegister registry);
}

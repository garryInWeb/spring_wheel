package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

/**
 * Created by zhengtengfei on 2018/9/13.
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistery implements ConfigurableBeanFactory {
    protected abstract Object createBean(BeanDefinition bd) throws BeanCreationException;
}

package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
public interface AutowireCapableBeanFactory extends BeanFactory{
    public Object resolveDenpendency(DependencyDescriptor dependencyDescriptor);
}

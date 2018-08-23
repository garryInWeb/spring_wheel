package org.litespring.beans.factory.config;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/6/29.
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {
    void setBeanClassLoader(ClassLoader classLoader);
    ClassLoader getBeanClassLoader();
    void addBeanPostProcessor(BeanPostProcessor postProcessor);
    List<BeanPostProcessor> getBeanPostProcessors();
}

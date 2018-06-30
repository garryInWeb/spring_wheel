package org.litespring.beans.factory.config;

/**
 * Created by zhengtengfei on 2018/6/29.
 */
public interface ConfigurableBeanFactory {
    void setBeanClassLoader(ClassLoader classLoader);
    ClassLoader getBeanClassLoader();
}

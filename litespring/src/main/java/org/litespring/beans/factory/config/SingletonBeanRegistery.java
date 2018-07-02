package org.litespring.beans.factory.config;

/**
 * Created by zhengtengfei on 2018/6/29.
 */
public interface SingletonBeanRegistery {
    void registerSingleton(String beanName,Object singletonBean);

    Object getSingletonBean(String beanName);

}

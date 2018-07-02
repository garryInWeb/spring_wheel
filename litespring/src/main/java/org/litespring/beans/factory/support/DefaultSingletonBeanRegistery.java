package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingletonBeanRegistery;
import org.litespring.utils.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengtengfei on 2018/6/29.
 */
public class DefaultSingletonBeanRegistery implements SingletonBeanRegistery {

    private Map<String, Object> singletonBeanMap = new ConcurrentHashMap<String,Object>();

    public void registerSingleton(String beanName, Object singletonBean) {
        Assert.notNull(beanName,"'beanName' not be null.'");

        Object oldBean = singletonBeanMap.get(beanName);
        if (oldBean != null){
            throw new IllegalStateException("Could not register object [" + singletonBean + "] under bean name '" + beanName +
                    "': there is already object [" + oldBean + "] exist");
        }
        this.singletonBeanMap.put(beanName,singletonBean);
    }

    public Object getSingletonBean(String beanName) {
        return this.singletonBeanMap.get(beanName);
    }
}

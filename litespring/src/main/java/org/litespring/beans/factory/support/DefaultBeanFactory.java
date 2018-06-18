package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.utils.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory,BeanDefinitionRegister {

    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public Object getBean(String beanId) {
        BeanDefinition bd = this.getBeanDefinition(beanId);
        if (bd == null)
            throw new BeanCreationException("Bean definition is not exist.");
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        try {
            Class<?> clazz = classLoader.loadClass(bd.getClassName());
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("Bean definition is not exist.");
        }
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(),beanDefinition);
    }
}

package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.utils.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistery implements ConfigurableBeanFactory,BeanDefinitionRegister {

    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader classLoader;

    public DefaultBeanFactory() {
    }

    public Object getBean(String beanId) {
        BeanDefinition bd = this.getBeanDefinition(beanId);
        if (bd == null)
            return null;
        if (bd.isSingleton()){
            Object bean = this.getSingletonBean(beanId);
            if (bean == null){
                bean = createBean(bd);
                this.registerSingleton(beanId,bean);
            }
            return bean;
        }
        return createBean(bd);
    }

    private Object createBean(BeanDefinition bd) {
        ClassLoader cl = this.getBeanClassLoader();
        try{
            Class<?> clazz = cl.loadClass(bd.getBeanClassName());
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("Create bean for " + bd.getBeanClassName() + "fail",e);
        }
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(),beanDefinition);
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.classLoader == null ?ClassUtils.getDefaultClassLoader():this.classLoader);
    }
}

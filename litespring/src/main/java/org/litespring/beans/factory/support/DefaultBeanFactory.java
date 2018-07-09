package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.utils.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放bean定义的map，继承的Singleton使他可以存放bean对应的类
 */
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
        Object bean = instantiateBean(bd);
        populate(bd,bean);
        return bean;
    }
    public void populate(BeanDefinition bd,Object bean){
        List<PropertyValue> propertys = bd.getPropertyValues();

        if (propertys == null || propertys.isEmpty())
            return;
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
            propertys.stream().forEach(property -> {
                String propertyName = property.getName();
                Object propertyValue = property.getValue();
                Object resolverValue = resolver.resolveValueIfNecessary(propertyValue);

                Arrays.stream(pds).forEach(pd -> {
                    if (pd.getName().equals(propertyName)){
                        try {
                            Object converterValue = simpleTypeConverter.converterIfNecessary(resolverValue,pd.getPropertyType());
                            pd.getWriteMethod().invoke(bean,converterValue);
                        } catch (Exception e){
                            throw new BeanCreationException("Failed to set resolverValue [" + resolverValue + "] for class [" + bd.getBeanClassName() + "]");
                        }
                    }
                });

            });
        }catch (Exception e){
            throw new BeanCreationException("Failed to obtain BeanInfo for class ["+ bd.getBeanClassName() +"]",e);
        }
    }

    private Object instantiateBean(BeanDefinition bd) {
        if (bd.hasConstructorArgumentValues()){
            ConstructorResolver constructorResolver = new ConstructorResolver(this);
            return constructorResolver.autowireConstructor(bd);
        }else {
            ClassLoader cl = this.getBeanClassLoader();
            try {
                Class<?> clazz = cl.loadClass(bd.getBeanClassName());
                return clazz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("Create bean for " + bd.getBeanClassName() + "fail", e);
            }
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

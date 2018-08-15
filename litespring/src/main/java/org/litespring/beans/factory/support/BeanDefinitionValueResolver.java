package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * 对 bean 的字段进行初始化的对象
 */
public class BeanDefinitionValueResolver {
    private final BeanFactory factory;

    public BeanDefinitionValueResolver(BeanFactory factory) {
        this.factory = factory;
    }

    /**
     * 对 bean 的字段进行初始化返回该对象
     *
     * @param value
     * @return
     */
    public Object resolveValueIfNecessary(Object value){
        // ref
        if (value instanceof RuntimeBeanReference){
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.factory.getBean(refName);
            return bean;
        // value
        }else if(value instanceof TypedStringValue){
            TypedStringValue refString = (TypedStringValue) value;
            return refString.getValue();
        }else{
            throw new RuntimeException("the value '" + value +"' has not implemented");
        }
    }
}

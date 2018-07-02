package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class BeanDefinitionValueResolver {
    private final DefaultBeanFactory factory;

    public BeanDefinitionValueResolver(DefaultBeanFactory factory) {
        this.factory = factory;
    }

    public Object resolveValueIfNecessary(Object value){
        if (value instanceof RuntimeBeanReference){
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.factory.getBean(refName);
            return bean;
        }else if(value instanceof TypedStringValue){
            TypedStringValue refString = (TypedStringValue) value;
            return refString.getValue();
        }else{
            throw new RuntimeException("the value '" + value +"' has not implemented");
        }
    }
}

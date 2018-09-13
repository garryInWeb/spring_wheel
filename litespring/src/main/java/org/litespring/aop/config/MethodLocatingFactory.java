package org.litespring.aop.config;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.FactoryBean;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.utils.BeanUtils;
import org.litespring.utils.StringUtils;

import java.lang.reflect.Method;

/**
 * 获取目标对象的方法对象的factory
 */
public class MethodLocatingFactory implements FactoryBean,BeanFactoryAware {

    private String targetBeanName;

    private String methodName;

    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getObject() {
        return method;
    }

    @Override
    public Class<?> getObjectType() {
        return this.method.getClass();
    }


    public void setBeanFactory(BeanFactory beanFactory) {
        if (!StringUtils.hasText(targetBeanName)){
            throw new IllegalArgumentException("Property 'targetBeanName' is required");
        }
        if (!StringUtils.hasText(methodName)){
            throw new IllegalArgumentException("Property 'methodName' is required");
        }

        Class<?> beanClass = beanFactory.getType(targetBeanName);
        if (beanClass == null){
            throw new IllegalArgumentException("Can't determine type of bean with name '" + targetBeanName + "'");
        }

        this.method = BeanUtils.resolveSignature(this.methodName,beanClass);

        if (this.method == null){
            throw new IllegalArgumentException("Unable to locate method '" + this.methodName +"' on bean '" + this.targetBeanName + "'");
        }

    }
}

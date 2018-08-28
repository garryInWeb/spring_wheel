package org.litespring.aop.config;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.utils.BeanUtils;
import org.litespring.utils.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/8/28.
 */
public class MethodLocatingFactory {

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

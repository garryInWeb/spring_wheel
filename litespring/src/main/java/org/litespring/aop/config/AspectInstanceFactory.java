package org.litespring.aop.config;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.utils.StringUtils;

/**
 * Created by zhengtengfei on 2018/9/4.
 */
public class AspectInstanceFactory implements BeanFactoryAware{
    private String aspectBeanName;

    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName){
        this.aspectBeanName = aspectBeanName;
    }

    public void setBeanFactory(BeanFactory factory){
        this.beanFactory = factory;
        if (!StringUtils.hasText(aspectBeanName)){
            throw new IllegalArgumentException("'aspectBeanName' is requested!");
        }
    }

    public Object getAspectInstance(){
        return this.beanFactory.getBean(this.aspectBeanName);
    }
}

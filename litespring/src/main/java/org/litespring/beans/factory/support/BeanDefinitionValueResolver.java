package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.FactoryBean;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * 对 bean 的字段进行初始化的对象
 */
public class BeanDefinitionValueResolver {
    private final AbstractBeanFactory factory;

    public BeanDefinitionValueResolver(AbstractBeanFactory factory) {
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
        // 合成 bean
        }else if(value instanceof BeanDefinition){
            BeanDefinition bd = (BeanDefinition) value;
            String innerBeanName = "(inner bean)" + bd.getBeanClassName() + "#" +
                    Integer.toHexString(System.identityHashCode(value));
            return resolveInnerBean(innerBeanName,bd);
        }else{
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBd) {
        try{
            Object innerBean = this.factory.createBean(innerBd);

            if (innerBean instanceof FactoryBean){
                try{
                    return ((FactoryBean<?>)innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName,"FactoryBean threw exception on object creation ",e);
                }
            }else{
                return innerBean;
            }
        }catch (BeansException e){
            throw new BeanCreationException(innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                            (innerBd != null && innerBd.getBeanClassName() != null ? "of type [" + innerBd.getBeanClassName() + "] " : "")
                    , e);
        }
    }
}

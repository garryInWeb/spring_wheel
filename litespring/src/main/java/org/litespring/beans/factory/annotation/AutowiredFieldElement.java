package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.utils.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
public class AutowiredFieldElement extends InjectionElement {

    boolean required;
    public AutowiredFieldElement(Field f, boolean required, AutowireCapableBeanFactory factory) {
        super(f,factory);
        this.required = required;
    }

    public Field getField(){return (Field) this.member;}
    @Override
    public void inject(Object target) {
        Field field = getField();
        try{
            DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(field,required);

            Object value = factory.resolveDenpendency(dependencyDescriptor);

            if (value != null){
                ReflectionUtils.makeAccessible(field);
                field.set(target,value);
            }
        } catch (Throwable ex) {
            throw new BeanCreationException("Could not autowire field:" + field,ex);
        }
    }
}

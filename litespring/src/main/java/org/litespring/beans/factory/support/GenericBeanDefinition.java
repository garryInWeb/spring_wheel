package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {
    private String id;
    private String beanClassName;
    private Class<?> beanClass;

    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;

    private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

    private ConstructorArgument constructorArgument = new ConstructorArgument();

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public boolean isSingleton() {
        return this.singleton;
    }

    public boolean isPrototype() {
        return this.prototype;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public String getId() {
        return id;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return constructorArgument;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    @Override
    public boolean hasBeanClass() {
        return beanClass != null;
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader beanClassLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null){
            return null;
        }
        Class<?> resolveClass = beanClassLoader.loadClass(className);
        this.beanClass = resolveClass;
        return resolveClass;

    }

    @Override
    public Class<?> getBeanClass() {
        if (beanClass == null){
            throw new IllegalStateException(
                    "Bean class name [" + this.getBeanClassName() + "] has not been resolved into an actual class"
            );
        }
        return beanClass;
    }

    public void setId(String id) {
        this.id = id;
    }
}

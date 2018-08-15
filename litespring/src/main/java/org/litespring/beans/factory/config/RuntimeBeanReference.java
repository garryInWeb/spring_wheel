package org.litespring.beans.factory.config;

/**
 * 依赖注入的抽象
 */

public class RuntimeBeanReference {
    private final String beanName;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}

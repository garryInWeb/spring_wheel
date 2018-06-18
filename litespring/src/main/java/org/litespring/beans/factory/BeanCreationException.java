package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

public class BeanCreationException extends BeansException {

    private String beanName;

    public BeanCreationException(String msg) {
        super(msg);
    }

    public BeanCreationException(String beanName,String msg) {
        super("Error creating bean with name " + beanName +":" + msg);
        this.beanName = beanName;
    }

    public BeanCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BeanCreationException(String beanName,String msg, Throwable cause) {
        super(msg, cause);
        initCause(cause);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
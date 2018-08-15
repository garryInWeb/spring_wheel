package org.litespring.beans;

/**
 * Created by zhengtengfei on 2018/7/6.
 */
public class TypeMismatchException extends Exception {
    private transient Object value;
    private Class<?> requiredType;

    public TypeMismatchException(Object value,Class<?> requiredType){
        super("Fail to converter value "+value+" to Class "+requiredType);
        this.value = value;
        this.requiredType = requiredType;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }
}

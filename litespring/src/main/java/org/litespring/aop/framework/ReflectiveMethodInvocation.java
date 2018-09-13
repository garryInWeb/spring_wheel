package org.litespring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 目标方法的代理类，再目标方法执行前循环执行一遍MethodInterceptor List
 */
public class ReflectiveMethodInvocation implements MethodInvocation{


    protected final Object targetObject;

    protected final Method targetMethod;

    protected Object[] arguments;

    protected final List<MethodInterceptor> interceptors;

    private int currentInterceptorsIndex = -1;


    public ReflectiveMethodInvocation(Object targetObject, Method targetMethod, Object[] arguments, List<MethodInterceptor> interceptors) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }

    @Override
    public Method getMethod() {
        return targetMethod;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * 执行目标方法
     * @return
     * @throws Throwable
     */
    protected Object invokeJoinpoint() throws Throwable {
        return this.targetMethod.invoke(targetObject,arguments);
    }

    /**
     * 循环执行MethodInterceptor
     * @return
     * @throws Throwable
     */
    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorsIndex == this.interceptors.size() - 1){
            return invokeJoinpoint();
        }
        this.currentInterceptorsIndex ++;
        MethodInterceptor methodInterceptor = this.interceptors.get(this.currentInterceptorsIndex);

        return methodInterceptor.invoke(this);

    }

    @Override
    public Object getThis() {
        return targetObject;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return targetMethod;
    }
}

package org.litespring.aop;


import org.aopalliance.intercept.MethodInterceptor;

/**
 * Created by zhengtengfei on 2018/9/1.
 */
public interface Advice extends MethodInterceptor {
    public Pointcut getPointcut();
}

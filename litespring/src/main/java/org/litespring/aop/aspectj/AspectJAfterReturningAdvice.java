package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/1.
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, AspectInstanceFactory factory) {
        super(adviceMethod, poincut, factory);
    }
    public Object invoke(MethodInvocation methodInvocation) throws Throwable{
        Object o = methodInvocation.proceed();
        this.invokeAdviceMethod();
        return o;
    }
}

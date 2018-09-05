package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/2.
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {
    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, AspectInstanceFactory factory) {
        super(adviceMethod, poincut, factory);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.invokeAdviceMethod();
        return invocation.proceed();
    }
}

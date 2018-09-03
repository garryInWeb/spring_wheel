package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/2.
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {
    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, Object adviceObject) {
        super(adviceMethod, poincut, adviceObject);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.invokeAdviceMethod();
        return invocation.proceed();
    }
}

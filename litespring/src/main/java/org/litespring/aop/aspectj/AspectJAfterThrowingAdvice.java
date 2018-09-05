package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/2.
 */
public class AspectJAfterThrowingAdvice  extends AbstractAspectJAdvice {

    public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, AspectInstanceFactory factory) {
        super(adviceMethod, poincut, factory);
    }
    public Object invoke(MethodInvocation methodInvocation) throws Throwable{
        try {
            return methodInvocation.proceed();
        }catch (Throwable e){
            this.invokeAdviceMethod();
            throw e;
        }
    }
}

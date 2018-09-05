package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/1.
 */
public abstract class AbstractAspectJAdvice implements Advice {

    protected Method adviceMethod;
    protected AspectJExpressionPoincut poincut;
    protected AspectInstanceFactory factory;

    public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, AspectInstanceFactory factory) {
        this.adviceMethod = adviceMethod;
        this.poincut = poincut;
        this.factory = factory;
    }

    public void invokeAdviceMethod() throws Throwable {
        this.adviceMethod.invoke(factory.getAspectInstance());
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Pointcut getPointcut() {
        return poincut;
    }

}

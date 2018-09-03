package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/9/1.
 */
public abstract class AbstractAspectJAdvice implements Advice {

    protected Method adviceMethod;
    protected AspectJExpressionPoincut poincut;
    protected Object adviceObject;

    public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPoincut poincut, Object adviceObject) {
        this.adviceMethod = adviceMethod;
        this.poincut = poincut;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws Throwable {
        this.adviceMethod.invoke(adviceObject);
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Pointcut getPointcut() {
        return poincut;
    }

}

package org.litespring.aop;

/**
 * Created by zhengtengfei on 2018/8/24.
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();
    String getExpression();
}

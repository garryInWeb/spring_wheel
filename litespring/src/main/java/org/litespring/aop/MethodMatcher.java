package org.litespring.aop;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/8/24.
 */
public interface MethodMatcher {
    boolean matches(Method method);
}

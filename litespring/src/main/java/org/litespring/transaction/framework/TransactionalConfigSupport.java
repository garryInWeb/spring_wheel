package org.litespring.transaction.framework;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.aop.framework.AopConfigSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public class TransactionalConfigSupport extends AopConfigSupport {
    private List<Method> methodWithTransaction = new ArrayList<>();

    public TransactionalConfigSupport(List<Method> methodWithTransaction) {
        super();
        this.methodWithTransaction = methodWithTransaction;
    }

    @Override
    public List<Advice> getAdvices(Method method) {
        if (methodWithTransaction.contains(method))
            return getAdvices();
        return null;
    }
}

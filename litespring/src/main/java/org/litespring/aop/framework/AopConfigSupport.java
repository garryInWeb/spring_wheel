package org.litespring.aop.framework;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.utils.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/3.
 */
public class AopConfigSupport implements AopConfig {


    private boolean proxyTargetClass = false;

    private Object targetObject = null;

    private List<Advice> advices = new ArrayList<>();

    private List<Class> interfaces = new ArrayList<>();

    public AopConfigSupport() {
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    @Override
    public Object getTargetObject() {
        return this.targetObject;
    }

    @Override
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass){
        this.proxyTargetClass = proxyTargetClass;
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class<?>[this.interfaces.size()]);
    }

    public void addInterface(Class<?> intf){
        Assert.notNull(intf,"Interface must not be null");
        if (!intf.isInterface()){
            throw new IllegalArgumentException("[ " + intf.getName() + " ] is not a interface!");
        }
        if (!interfaces.contains(intf)){
            interfaces.add(intf);
        }
    }

    @Override
    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : interfaces){
            if (intf.isAssignableFrom(proxyIntf))
                return true;
        }
        return false;
    }

    @Override
    public List<Advice> getAdvices() {
        return this.advices;
    }

    @Override
    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }

    @Override
    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<>();
        for (Advice advice : this.advices){
            Pointcut pc = advice.getPointcut();
            if (pc.getMethodMatcher().matches(method)){
                result.add(advice);
            }
        }
        return result;
    }

    @Override
    public void setTargetObject(Object object) {
        this.targetObject = object;
    }
}

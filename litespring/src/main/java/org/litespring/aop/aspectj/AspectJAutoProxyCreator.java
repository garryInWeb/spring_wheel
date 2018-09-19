package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhengtengfei on 2018/9/18.
 */
public class AspectJAutoProxyCreator implements BeanPostProcessor{

    private ConfigurableBeanFactory beanFactory;

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object afterInitialization(Object result, String beanName) {
        if (isInfrastructureClass(result.getClass())){
            return result;
        }
        List<Advice> advices = getCandidateAdvices(result);
        if (advices.isEmpty()){
            return result;
        }
        return createProxy(advices,result);
    }

    private Object createProxy(List<Advice> advices, Object result) {

        AopConfigSupport config = new AopConfigSupport();
        for (Advice advice : advices){
            config.addAdvice(advice);
        }
        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(result.getClass());
        for (Class<?> targetInterface : targetInterfaces){
            config.addInterface(targetInterface);
        }
        config.setTargetObject(result);

        AopProxyFactory aopProxyFactory = null;
        if (config.getProxiedInterfaces().length == 0){
            aopProxyFactory = new CglibProxyFactory(config);
        }else{

        }
        return aopProxyFactory.getProxy();
    }

    private List<Advice> getCandidateAdvices(Object result) {
        List<Object> advices = this.beanFactory.getBeansByType(Advice.class);

        List<Advice> adviceList = new ArrayList<>();
        for (Object o : advices){
            Pointcut pointcut = ((Advice) o).getPointcut();
            if (canApply(pointcut,result.getClass())){
                adviceList.add((Advice) o);
            }
        }
        return adviceList;
    }

    private boolean canApply(Pointcut pointcut, Class<?> targetClass) {
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        Set<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
        classes.add(targetClass);
        for (Class clazz : classes){
            Method[] methods = clazz.getMethods();
            for (Method method : methods){
                if(methodMatcher.matches(method)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass);
    }
}

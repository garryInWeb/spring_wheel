package org.litespring.transaction.annotation;

import org.litespring.aop.Advice;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.transaction.framework.TransactionalConfigSupport;
import org.litespring.transaction.handler.JDBCHandler;
import org.litespring.transaction.support.TransactionInstanceFactory;
import org.litespring.utils.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public class TransactionalAnnotationProcessor implements BeanPostProcessor {

    private Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();

    public TransactionalAnnotationProcessor() {
        this.autowiredAnnotationTypes.add(Transactional.class);
    }

    @Override
    public Object afterInitialization(Object result, String beanName) {
        // 1 判断方法中是否包含 Transactional 注解,得到全部带有Transactional注解的方法
        List<Method> methodWithTransaction = checkMethodTransaction(result.getClass());
        if (methodWithTransaction.isEmpty()){
            return result;
        }
        // 获得 advice
        List<Advice> advices = getCandidateAdvices();

        // 生成代理

        return createProxy(advices,result,methodWithTransaction);
    }

    private List<Advice> getCandidateAdvices(){
        JDBCHandler jdbcHandler = new JDBCHandler();
        TransactionInstanceFactory factory = new TransactionInstanceFactory(jdbcHandler);
        Class clazz = jdbcHandler.getClass();
        AspectJBeforeAdvice aspectJBeforeAdvice = null;
        AspectJAfterReturningAdvice aspectJAfterReturningAdvice = null;
        AspectJAfterThrowingAdvice aspectJAfterThrowingAdvice = null;
        try {
            aspectJBeforeAdvice = new AspectJBeforeAdvice(clazz.getMethod("transactionBefore"),null,factory);
            aspectJAfterThrowingAdvice = new AspectJAfterThrowingAdvice(clazz.getMethod("transactionThrowing"),null,factory);
            aspectJAfterReturningAdvice = new AspectJAfterReturningAdvice(clazz.getMethod("transactionAfter"),null,factory);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        List<Advice> result = new ArrayList<>();
        result.add(aspectJBeforeAdvice);
        result.add(aspectJAfterReturningAdvice);
        result.add(aspectJAfterThrowingAdvice);
        return result;
    }

    private Object createProxy(List<Advice> advices, Object result,List<Method> methodWithTransaction) {
        TransactionalConfigSupport config = new TransactionalConfigSupport(methodWithTransaction);
        for (Advice advice : advices){
            config.addAdvice(advice);
        }
        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(result.getClass());
        for (Class<?> targetInterface : targetInterfaces){
            config.addInterface(targetInterface);
        }
        config.setTargetObject(result);

        AopProxyFactory aopProxyFactory = null;
        aopProxyFactory = new CglibProxyFactory(config);

        return aopProxyFactory.getProxy();
    }

    public List checkMethodTransaction(Class<?> clazz) {
        LinkedList<Method> elements = new LinkedList<>();
        Class<?> targetClass = clazz;
//        do {
            for (Method method : targetClass.getMethods()) {
                Annotation ann = findAutowiredAnnotation(method);
                if (ann != null){
                    elements.add(method);
                }
            }
//        } while(targetClass != null && targetClass != Object.class);

        return elements;
    }


    private Annotation findAutowiredAnnotation(AccessibleObject field) {
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes){
            Annotation ann = AnnotationUtils.getAnnotation(field,type);
            if (ann != null)
                return ann;
        }
        return null;
    }
}

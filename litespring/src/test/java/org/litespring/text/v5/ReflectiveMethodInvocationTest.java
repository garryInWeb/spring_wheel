package org.litespring.text.v5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.framework.ReflectiveMethodInvocation;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法代理器的测试
 */
public class ReflectiveMethodInvocationTest extends AbstractV5Test{

    private AspectJBeforeAdvice aspectJBeforeAdvice;
    private AspectJAfterReturningAdvice aspectJAfterReturningAdvice;
    private AspectJAfterThrowingAdvice aspectJAfterThrowingAdvice;
    private PetStoreService petStoreService;

    private AspectInstanceFactory aspectInstanceFactory = null;
    private BeanFactory factory = null;


    private TransactionManager tx;

    @Before
    public void setUp() throws NoSuchMethodException {

        factory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(factory);

        petStoreService = new PetStoreService();
        tx = new TransactionManager();
        MessageTracker.clearMsgs();

        aspectJBeforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                null,
                aspectInstanceFactory
                );
        aspectJAfterReturningAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                null,
                aspectInstanceFactory
        );
        aspectJAfterThrowingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"),
                null,
                aspectInstanceFactory
        );
    }

    @Test
    public void testMethodInvocation() throws Throwable{
        Method targetmethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();

        interceptors.add(aspectJBeforeAdvice);
        interceptors.add(aspectJAfterReturningAdvice);

        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetmethod,new Object[0],interceptors);
        mi.proceed();

        Assert.assertTrue(MessageTracker.getMsgs().size() == 3);
        Assert.assertTrue("start tx".equals(MessageTracker.getMsgs().get(0)));
        Assert.assertTrue("commit tx".equals(MessageTracker.getMsgs().get(2)));

    }

    @Test
    public void testMethodInvocation1() throws Throwable{
        Method targetmethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<>();

        interceptors.add(aspectJBeforeAdvice);
        interceptors.add(aspectJAfterReturningAdvice);
        interceptors.add(aspectJAfterThrowingAdvice);

        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetmethod,new Object[0],interceptors);
        try {
            mi.proceed();
        }catch (Exception e){
            Assert.assertTrue(MessageTracker.getMsgs().size() == 2);
            Assert.assertTrue("start tx".equals(MessageTracker.getMsgs().get(0)));
            Assert.assertTrue("rollback tx".equals(MessageTracker.getMsgs().get(1)));
        }
    }
}

package org.litespring.text.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPoincut;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/3.
 */
public class CglibAopProxyTest {

    private static AspectJBeforeAdvice beforeAdvice = null;
    private static AspectJAfterReturningAdvice afterAdvice = null;
    private static AspectJExpressionPoincut pc = null;

    private TransactionManager tx;

    @Before
    public void setUp() throws NoSuchMethodException {
        tx = new TransactionManager();
        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";
        pc = new AspectJExpressionPoincut();
        pc.setExperssion(expression);

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                pc,
                tx
        );
        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                pc,
                tx
        );
    }

    @Test
    public void testGetProxy(){
        AopConfig config = new AopConfigSupport();

        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);
        config.setTargetObject(new PetStoreService());

        CglibProxyFactory proxyFactory = new CglibProxyFactory(config);

        PetStoreService proxy = (PetStoreService)proxyFactory.getProxy();
        proxy.placeOrder();

        List<String> msg = MessageTracker.getMsgs();
        Assert.assertTrue(msg.size() == 3);

        proxy.toString();

    }
}

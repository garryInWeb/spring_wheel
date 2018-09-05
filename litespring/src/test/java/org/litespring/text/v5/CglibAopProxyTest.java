package org.litespring.text.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPoincut;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/3.
 */
public class CglibAopProxyTest extends AbstractV5Test{

    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJExpressionPoincut pc = null;
    private BeanFactory factory = null;

    private AspectInstanceFactory aspectInstanceFactory = null;

    private TransactionManager tx;

    @Before
    public void setUp() throws NoSuchMethodException {

        MessageTracker.clearMsgs();

        tx = new TransactionManager();
        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";
        pc = new AspectJExpressionPoincut();
        pc.setExperssion(expression);

        factory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(factory);

        beforeAdvice = new AspectJBeforeAdvice(
                getAdviceMethod("start"),
                pc,
                aspectInstanceFactory
        );
        afterAdvice = new AspectJAfterReturningAdvice(
                getAdviceMethod("commit"),
                pc,
                aspectInstanceFactory
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

package org.litespring.text.v5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.Advice;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.tx.TransactionManager;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/13.
 */
public class BeanFactoryTest5 extends AbstractV5Test {
    static String expectedExpression = "execution(* org.litespring.service.v5.*.placeOrder(..))";

    @Test
    public void testGetBeanByType() throws NoSuchMethodException {
        BeanFactory factory = super.getBeanFactory("petstore-v5.xml");
        List<Object> advices = factory.getBeansByType(Advice.class);
        Assert.assertEquals(3,advices.size());

        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice)this.getAdvice(AspectJBeforeAdvice.class,advices);

            Assert.assertEquals(TransactionManager.class.getMethod("start"),advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdvicesInstance().getClass());
        }
        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice)this.getAdvice(AspectJAfterReturningAdvice.class,advices);

            Assert.assertEquals(TransactionManager.class.getMethod("commit"),advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdvicesInstance().getClass());
        }
        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice)this.getAdvice(AspectJAfterThrowingAdvice.class,advices);

            Assert.assertEquals(TransactionManager.class.getMethod("rollback"),advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdvicesInstance().getClass());
        }
    }

    public Object getAdvice(Class<?> type,List<Object> advices){
        for (Object o : advices){
            if (o.getClass().equals(type)){
                return o;
            }
        }
        return null;
    }
}

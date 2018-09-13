package org.litespring.text.v5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPoincut;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.config.MethodLocatingFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.tx.TransactionManager;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/6.
 */
public class BeanDefinitionTestV5 extends AbstractV5Test {

    @Test
    public void testAOPBean(){
        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");

        {
            BeanDefinition bd = factory.getBeanDefinition("tx");
            Assert.assertTrue(bd.getBeanClassName().equals(TransactionManager.class.getName()));
        }
        //检查placeOrder是否正确生成
        {
            BeanDefinition bd = factory.getBeanDefinition("placeOrder");
            //这个Bean是否合成
            Assert.assertTrue(bd.isSynthetic());
            Assert.assertTrue(bd.getBeanClass().equals(AspectJExpressionPoincut.class));

            PropertyValue propertyValue = bd.getPropertyValues().get(0);
            Assert.assertEquals("expression",propertyValue.getName());
            Assert.assertEquals("execution(* org.litespring.service.v5.*.placeOrder(..))",propertyValue.getValue());
        }

        //检查AspectJBeforeAdvice
        {
            String name = AspectJBeforeAdvice.class.getName() + "#0";
            BeanDefinition bd = factory.getBeanDefinition(name);
            Assert.assertTrue(bd.getBeanClass().equals(AspectJBeforeAdvice.class));

            //这个BeanDefinition是合成的
            Assert.assertTrue(bd.isSynthetic());

            List<ConstructorArgument.ValueHolder> args = bd.getConstructorArgument().getValueHolderList();
            Assert.assertEquals(3,args.size());
            {
                BeanDefinition innerBeanDef = (BeanDefinition)args.get(0).getValue();
                Assert.assertTrue(innerBeanDef.isSynthetic());
                Assert.assertTrue(innerBeanDef.getBeanClass().equals(MethodLocatingFactory.class));

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                Assert.assertEquals("targetBeanName",pvs.get(0).getName());
                Assert.assertEquals("tx",pvs.get(0).getValue());
                Assert.assertEquals("methodName",pvs.get(1).getName());
                Assert.assertEquals("start",pvs.get(1).getValue());
            }

            {
                RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
                Assert.assertEquals("placeOrder",ref.getBeanName());
            }
            {
                BeanDefinition innerBeanDef = (BeanDefinition)args.get(2).getValue();
                Assert.assertTrue(innerBeanDef.isSynthetic());
                Assert.assertTrue(innerBeanDef.getBeanClass().equals(AspectInstanceFactory.class));

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                Assert.assertEquals("aspectBeanName",pvs.get(0).getName());
                Assert.assertEquals("tx",pvs.get(0).getValue());
            }

        }
    }
}

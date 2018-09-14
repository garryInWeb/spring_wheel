package org.litespring.text.v5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.aspectj.AspectJExpressionPoincut;
import org.litespring.service.v5.PetStoreService;

import java.lang.reflect.Method;

/**
 * 方法与表达式匹配的测试用例
 */
public class PoincutTest {
    @Test
    public void testPoincut() throws NoSuchMethodException {

        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";

        AspectJExpressionPoincut pc = new AspectJExpressionPoincut();
        pc.setExpression(expression);

        MethodMatcher mm = pc.getMethodMatcher();

        {
            Class<?> targetClass = PetStoreService.class;
            Method method = targetClass.getMethod("placeOrder");
            Assert.assertTrue(mm.matches(method));

            Method method1 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(mm.matches(method1));

        }

        {
            Class<?> targetClass = org.litespring.service.v4.PetStoreService.class;
            Method method1 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(mm.matches(method1));
        }
    }
}

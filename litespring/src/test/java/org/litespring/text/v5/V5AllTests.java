package org.litespring.text.v5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengtengfei on 2018/9/5.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTest5.class,
        CglibAopProxyTest.class,
        CGLibTest.class,
        MethodLocatingFactoryTest.class,
        PoincutTest.class,
        ReflectiveMethodInvocationTest.class,
        BeanDefinitionTestV5.class,
        BeanFactoryTest5.class,
}
)
public class V5AllTests {
}

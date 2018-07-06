package org.litespring.text.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV2.class,
        BeanDefinitionTestV2.class,
        BeanDefinitionValueResolverTest.class
}
)
public class V2ALLTests {
}

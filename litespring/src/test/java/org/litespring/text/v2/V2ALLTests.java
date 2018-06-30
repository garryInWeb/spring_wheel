package org.litespring.text.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.text.v1.ApplicationContextTest;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV2.class,
        BeanDefinitionTestV2.class
}
)
public class V2ALLTests {
}

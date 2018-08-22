package org.litespring.text;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.text.v1.V1AllTests;
import org.litespring.text.v2.V2ALLTests;
import org.litespring.text.v3.V3ALLTests;
import org.litespring.text.v4.*;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTests.class,
        V2ALLTests.class,
        V3ALLTests.class,
        V4ALLTests.class
}
)
public class ALLTest {
}

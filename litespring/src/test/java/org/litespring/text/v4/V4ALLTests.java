package org.litespring.text.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTest4.class,
        ClassPathBeanDefinitionScannerTest.class,
        ClassReaderTest.class,
        DependencyDescriptorTest.class,
        InjectionMetadataTest.class,
        MetadataReaderTest.class,
        PackageResourceLoaderTest.class,
        XmlBeanDefinitionReaderTest.class
}
)
public class V4ALLTests {
}

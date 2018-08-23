package org.litespring.text.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.text.v3.ApplicationContextTestV3;
import org.litespring.text.v3.BeanDefinitionTestV3;
import org.litespring.text.v3.ConstructorResolverTest;

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

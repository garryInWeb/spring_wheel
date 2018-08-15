package org.litespring.text.v3;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.service.v3.PetStoreService;

/**
 * Created by zhengtengfei on 2018/7/9.
 */
public class ConstructorResolverTest {
    @Test
    public void testAutowireConstructor(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");
        ConstructorResolver resolver = new ConstructorResolver(factory);

        PetStoreService petStoreService = (PetStoreService)resolver.autowireConstructor(bd);
        Assert.assertNotNull(petStoreService.getItemDao());
        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertEquals(1,petStoreService.getVersion());
    }
}

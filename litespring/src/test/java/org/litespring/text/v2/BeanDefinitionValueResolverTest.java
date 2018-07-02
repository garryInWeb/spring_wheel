package org.litespring.text.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.BeanDefinitionValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v2.AccountDao;
import org.litespring.service.v2.PetStoreService;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class BeanDefinitionValueResolverTest {

    @Test
    public void testResolverRuntimeBeanReference(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        RuntimeBeanReference accountDao = new RuntimeBeanReference("accountDao");

        BeanDefinitionValueResolver beanDefinitionValueResolver = new BeanDefinitionValueResolver(factory);
        Assert.assertTrue(beanDefinitionValueResolver.resolveValueIfNecessary(accountDao) instanceof AccountDao);
    }
    @Test
    public void testTypedStringValue(){

    }
}

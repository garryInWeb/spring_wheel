package org.litespring.text.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.annotation.AutowiredFieldElement;
import org.litespring.beans.factory.annotation.InjectionElement;
import org.litespring.beans.factory.annotation.InjectionMetadata;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.dao.v4.ItemDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
public class InjectionMetadataTest {
    @Test
    public void testInjection() throws NoSuchFieldException {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Class<?> clazz = PetStoreService.class;
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();

        {
            Field f = PetStoreService.class.getDeclaredField("accountDao");
            InjectionElement injectionEle = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionEle);
        }
        {
            Field f = PetStoreService.class.getDeclaredField("itemDao");
            InjectionElement injectionEle = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionEle);
        }

        InjectionMetadata metadata = new InjectionMetadata(clazz,elements);

        PetStoreService petStoreService = new PetStoreService();

        metadata.inject(petStoreService);

        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);


    }
}

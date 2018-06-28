package org.litespring.text.v1;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XMLBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.*;

public class BeanFactoryTest {

    DefaultBeanFactory factory;
    XMLBeanDefinitionReader reader;
    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        reader = new XMLBeanDefinitionReader(factory);
    }
    @Test
    public void testBeanFactory(){

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

//        assertTrue(bd.isSingleton());
//
//        assertTrue(bd.isPrototype());
//
//        assertEquals(BeanDefinition.SCOPE_DEFAULT,bd.getScope());

        assertEquals("org.litespring.service.v1.PetStoreService",bd.getClassName());

        PetStoreService petStoreService = (PetStoreService)factory.getBean("petStore");

        assertNotNull(petStoreService);

        PetStoreService petStoreService1 = (PetStoreService)factory.getBean("petStore");

        assertTrue(petStoreService.equals(petStoreService1));
    }

    @Test
    public void testBeanCreationException(){

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        try{
            factory.getBean("abc");
        }catch (BeanCreationException e){
            return;
        }
        fail("exception BeanCreationException");
    }

    @Test
    public void testBeanDefinitionException(){
        try{
            reader.loadBeanDefinitions(new ClassPathResource("abv.xml"));
        }catch (BeanDefinitionStoreException e){
            return;
        }
        fail("exception BeanDefinitionException");
    }
}

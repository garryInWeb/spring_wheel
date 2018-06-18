package org.litespring.text.v1;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XMLBeanDefinitionReader;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BeanFactoryTest {

    DefaultBeanFactory factory;
    XMLBeanDefinitionReader xmlBeanDefinitionReader;
    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        xmlBeanDefinitionReader = new XMLBeanDefinitionReader(factory);
    }
    @Test
    public void testBeanFactory(){

        xmlBeanDefinitionReader.loadBeanDefinitions("petstore-v1.xml");

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertEquals("org.litespring.service.v1.PetStoreService",bd.getClassName());

        PetStoreService petStoreService = (PetStoreService)factory.getBean("petStore");

        assertNotNull(petStoreService);
    }

    @Test
    public void testBeanCreationException(){

        xmlBeanDefinitionReader.loadBeanDefinitions("petstore-v1.xml");

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
            xmlBeanDefinitionReader.loadBeanDefinitions("abv.xml");
        }catch (BeanDefinitionStoreException e){
            return;
        }
        fail("exception BeanDefinitionException");
    }
}

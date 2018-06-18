package org.litespring.text.v1;

import org.junit.Test;
import org.litespring.beans.context.ApplicationContext;
import org.litespring.beans.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertNotNull;

public class ApplicationContextTest {

    @Test
    public void testApplicationContext(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService)applicationContext.getBean("petStore");

        assertNotNull(petStoreService);
    }
}

package org.litespring.text.v2;

import javafx.application.Application;
import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v2.PetStoreService;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class ApplicationContextTestV2 {
    @Test
    public void testGetBeanProperty(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStoreService = (PetStoreService)applicationContext.getBean("petStore");

        Assert.assertNotNull(petStoreService);
        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());

    }
}

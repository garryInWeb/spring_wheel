package org.litespring.text.v6;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v6.IPetStoreService;
import org.litespring.util.MessageTracker;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class ApplicationTestV6 {
    @Test
    public void testGetBeanProperty(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v6.xml");
        IPetStoreService iPetStoreService = (IPetStoreService)applicationContext.getBean("petStore");

        iPetStoreService.placeOrder();

        List<String> msg = MessageTracker.getMsgs();
        Assert.assertEquals(3,msg.size());
    }
}

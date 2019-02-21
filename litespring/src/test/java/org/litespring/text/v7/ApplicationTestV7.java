package org.litespring.text.v7;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v7.PetStoreService;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class ApplicationTestV7 {
    @Test
    public void testGetBeanProperty() throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v7.xml");
        applicationContext.getBean("dataSource");
        PetStoreService iPetStoreService = (PetStoreService)applicationContext.getBean("petStore");
        try {
            iPetStoreService.placeOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

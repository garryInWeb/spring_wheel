package org.litespring.text.v1;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertNotNull;

/**
 * 测试 ClassPathXmlApplicationContext 和 FileSystemXmlApplicationContext
 * ClassPathXmlApplicationContext 通过 ClassLoader 对配置文件进行读取
 * FileSystemXmlApplicationContext 通过 File 对配置文件进行读取
 */
public class ApplicationContextTest {

    @Test
    public void testClassPathApplicationContext(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService)applicationContext.getBean("petStore");

        assertNotNull(petStoreService);
    }
    @Test
    public void testFileSystemApplicationContext(){
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("D:\\project\\spring_wheel\\litespring\\src\\test\\resource\\petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService)applicationContext.getBean("petStore");

        assertNotNull(petStoreService);
    }
}

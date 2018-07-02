package org.litespring.text.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengtengfei on 2018/6/27.
 */
public class ResourceTest {
    @Test
    public void testClassPathResource() throws IOException {
        Resource resource = new ClassPathResource("petstore-v1.xml");

        InputStream in = null;
        try{
            in = resource.getInputStream();
            Assert.assertNotNull(in);
        }finally {
            if (in != null)
                in.close();
        }
    }
    @Test
    public void testFileSystemResource() throws IOException {
        Resource resource = new FileSystemResource("D:\\project\\spring_wheel\\litespring\\src\\test\\resource\\petstore-v1.xml");

        InputStream in = null;
        try{
            in = resource.getInputStream();
            Assert.assertNotNull(in);
        }finally {
            if (in != null)
                in.close();
        }
    }
}

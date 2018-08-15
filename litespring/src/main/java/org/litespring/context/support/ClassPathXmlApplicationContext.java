package org.litespring.context.support;

import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;

/**
 * 通过 ClassLoader 读取配置文件的 context
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    protected Resource getResourceByPath(String configFile) {
        return new ClassPathResource(configFile,this.getBeanClassLoader());
    }

}

package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public abstract class AbstractApplicationContext implements ApplicationContext{
    private DefaultBeanFactory factory = null;
    private ClassLoader classLoader;

    public AbstractApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
    }

    protected abstract Resource getResourceByPath(String configFile);

    public Object getBean(String beanId) {
        return factory.getBean(beanId);
    }

    public void setBeanClassLoader(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    public ClassLoader getBeanClassLoader(){
        return this.classLoader == null ? ClassUtils.getDefaultClassLoader() : this.classLoader;
    }
}

package org.litespring.context.support;

import org.litespring.context.ApplicationContext;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XMLBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;

public class ClassPathXmlApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory = null;

    public ClassPathXmlApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource(configFile));
    }

    public Object getBean(String beanId) {
        return factory.getBean(beanId);
    }
}

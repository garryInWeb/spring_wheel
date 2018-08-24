package org.litespring.context.support;

import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

/**
 * context 抽象类，在 new 对象时就会通过配置文件初始化 factory
 */
public abstract class AbstractApplicationContext implements ApplicationContext{
    private DefaultBeanFactory factory = null;
    private ClassLoader classLoader;

    public AbstractApplicationContext(String configFile) {
        // 存放 bean 定义的工厂
        factory = new DefaultBeanFactory();
        // 对配置文件进行解析和加载factory的类
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        // 获取配置文件的抽象输入
        Resource resource = this.getResourceByPath(configFile);
        // 通过 Resource 提供的 IO 流对配置文件进行解析
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
        registerBeanPostProcessors(factory);
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
    public void registerBeanPostProcessors(ConfigurableBeanFactory factory){
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(factory);
        factory.addBeanPostProcessor(processor);
    }
}

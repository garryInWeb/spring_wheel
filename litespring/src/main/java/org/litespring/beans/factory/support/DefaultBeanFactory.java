package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.utils.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放bean定义的map，继承的Singleton使他可以存放bean对应的对象
 */
public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegister {

    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader classLoader;
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();


    public DefaultBeanFactory() {
    }

    /**
     * 通过 id 获取 bean 实体对象
     * @param beanId
     * @return
     */
    public Object getBean(String beanId) {
        BeanDefinition bd = this.getBeanDefinition(beanId);
        if (bd == null)
            return null;
        // 配置文件的单例判断
        if (bd.isSingleton()){
            Object bean = this.getSingletonBean(beanId);
            if (bean == null){
                bean = createBean(bd);
                this.registerSingleton(beanId,bean);
            }
            return bean;
        }
        return createBean(bd);
    }

    /**
     * 创建 beanDefinition 对应的 bean实例
     * @param bd
     * @return
     */
    protected Object createBean(BeanDefinition bd) {
        Object bean = instantiateBean(bd);
        populate(bd,bean);

        bean = initializeBean(bd,bean);
        return bean;
    }

    protected Object initializeBean(BeanDefinition bd, Object bean) {
        invokeAwareMethods(bean);
        if (!bd.isSynthetic()){
            return applyBeanPostProcessorAfterInitialzation(bean,bd.getId());
        }
        return bean;
    }

    private Object applyBeanPostProcessorAfterInitialzation(Object bean, String beanName) {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()){
            result = beanPostProcessor.afterInitialization(result,beanName);
            if (result == null){
                return result;
            }
        }
        return result;
    }

    private void invokeAwareMethods(Object bean) {
        if (bean instanceof BeanFactoryAware){
            ((BeanFactoryAware)bean).setBeanFactory(this);
        }
    }

    public void populate(BeanDefinition bd,Object bean){
        // 处理 autowire 自动注入
        for (BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()){
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(bean,bd.getId());
            }
        }

        List<PropertyValue> propertys = bd.getPropertyValues();

        if (propertys == null || propertys.isEmpty())
            return;
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
            propertys.stream().forEach(property -> {
                String propertyName = property.getName();
                Object propertyValue = property.getValue();
                // 对对象字段进行赋值
                Object resolverValue = resolver.resolveValueIfNecessary(propertyValue);

                Arrays.stream(pds).forEach(pd -> {
                    if (pd.getName().equals(propertyName)){
                        try {
                            Object converterValue = simpleTypeConverter.converterIfNecessary(resolverValue,pd.getPropertyType());
                            pd.getWriteMethod().invoke(bean,converterValue);
                        } catch (Exception e){
                            throw new BeanCreationException("Failed to set resolverValue [" + resolverValue + "] for class [" + bd.getBeanClassName() + "]");
                        }
                    }
                });

            });
        }catch (Exception e){
            throw new BeanCreationException("Failed to obtain BeanInfo for class ["+ bd.getBeanClassName() +"]",e);
        }
    }

    /**
     * 通过构造函数或者反射 new bean对象
     * @param bd
     * @return
     */
    private Object instantiateBean(BeanDefinition bd) {
        // 通过判断是否含有构造参数确定是否调用有参进行构造
        if (bd.hasConstructorArgumentValues()){
            ConstructorResolver constructorResolver = new ConstructorResolver(this);
            return constructorResolver.autowireConstructor(bd);
        }else {
            ClassLoader cl = this.getBeanClassLoader();
            try {
                Class<?> clazz = cl.loadClass(bd.getBeanClassName());
                return clazz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("Create bean for " + bd.getBeanClassName() + "fail", e);
            }
        }
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(),beanDefinition);
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.classLoader == null ?ClassUtils.getDefaultClassLoader():this.classLoader);
    }

    /**
     * 循环bd的map，匹配传入的对象
     * @param descriptor
     * @return
     */
    @Override
    public Object resolveDenpendency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDenpendencyType();

        for (BeanDefinition bd : this.beanDefinitionMap.values()){
            //确保beanDefinition对象有class
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)){
                return this.getBean(bd.getId());
            }
        }
        return null;
    }

    /**
     * 对没有对象的beanDefinition赋值
     * @param bd
     */
    private void resolveBeanClass(BeanDefinition bd) {
        if (bd.hasBeanClass()){
            return;
        }else{
            try{
                bd.resolveBeanClass(this.getBeanClassLoader());
            }catch (ClassNotFoundException e){
                throw new RuntimeException("can't load class " + bd.getBeanClassName());
            }
        }
    }
    public void addBeanPostProcessor(BeanPostProcessor postProcessor){
        this.beanPostProcessors.add(postProcessor);
    }
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public Class<?> getType(String name){
        BeanDefinition bd = this.getBeanDefinition(name);
        if (bd == null){
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<>();
        List<String> beansId = this.getBeanIDsByType(type);
        for (String beanID : beansId){
            result.add(this.getBean(beanID));
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionMap.keySet()){
            Class<?> beanClass = null;
            try{
                beanClass = this.getType(beanName);
            }catch (Exception e){
                continue;
            }
            if ((beanClass != null) && type.isAssignableFrom(beanClass)){
                result.add(beanName);
            }
        }
        return result;
    }

}

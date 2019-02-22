package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/7/9.
 */
public class ConstructorResolver {

    protected final Log logger = LogFactory.getLog(getClass());

    private final AbstractBeanFactory beanFactory;

    public ConstructorResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(final BeanDefinition bd) {

        Constructor<?> constructorToUser = null;
        Object[] argsToUser = null;
        Class<?> beanClass = null;
        try{
            // 获取 class 对象
            beanClass = beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(bd.getBeanClassName(),"Instantiation of bean fail,can't resolve class",e);
        }
        // 获取所有构造器对象
        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this.beanFactory);

        ConstructorArgument constructorArgument = bd.getConstructorArgument();
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();


        for (int i = 0; i < candidates.length; i++){
            Class<?> [] parameters = candidates[i].getParameterTypes();
            if (parameters.length != constructorArgument.getArgumentCount()){
                continue;
            }
            argsToUser = new Object[parameters.length];

            boolean result = this.valueMatchTypes(parameters,
                    constructorArgument.getValueHolderList(),
                    argsToUser,
                    resolver,
                    simpleTypeConverter);
            if (result){
                constructorToUser = candidates[i];
                break;
            }
        }
        if (constructorToUser == null){
            throw new BeanCreationException(bd.getBeanClassName(),"can't find a apporiate constructor.");
        }
        try{
            return constructorToUser.newInstance(argsToUser);
        }catch (Exception e){
            throw new BeanCreationException(bd.getBeanClassName(),"can't find a create instance using"+ constructorToUser);
        }
    }

    private boolean valueMatchTypes(Class<?>[] parameters,
                                    List<ConstructorArgument.ValueHolder> valueHolderList,
                                    Object[] argsToUser,
                                    BeanDefinitionValueResolver resolver,
                                    SimpleTypeConverter simpleTypeConverter) {
        for (int i = 0; i < parameters.length; i ++){
            ConstructorArgument.ValueHolder valueHolder = valueHolderList.get(i);
            Object orignialValue = valueHolder.getValue();
            try{
                //获得真正的值
                Object resvoledValue = resolver.resolveValueIfNecessary(((ConstructorArgument.ValueHolder)orignialValue).getValue());
                // 转型
                Object convertedValue = simpleTypeConverter.converterIfNecessary(resvoledValue,parameters[i]);
                // 转型成功，记录返回
                argsToUser[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }
        }
        return true;
    }
}

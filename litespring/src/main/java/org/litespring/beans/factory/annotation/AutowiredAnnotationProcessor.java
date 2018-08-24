package org.litespring.beans.factory.annotation;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.utils.AnnotationUtils;
import org.litespring.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by zhengtengfei on 2018/8/22.
 */
public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor{
    private AutowireCapableBeanFactory beanFactory;
    private Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();
    private String requiredParamterName = "required";


    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    public AutowiredAnnotationProcessor(){
        this.autowiredAnnotationTypes.add(Autowired.class);
    }

    public InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        LinkedList<InjectionElement> elements = new LinkedList<>();
        Class<?> targetClass = clazz;
        do{
            LinkedList<InjectionElement> currElements = new LinkedList<>();
            for (Field field : targetClass.getDeclaredFields()){
                Annotation ann = findAutowiredAnnotation(field);
                if (ann != null){
                    if (Modifier.isStatic(field.getModifiers())){
                        continue;
                    }
                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new AutowiredFieldElement(field,required,beanFactory));
                }
            }
            // TODO 处理方法注入
            elements.addAll(currElements);
            targetClass = targetClass.getSuperclass();
        }while(targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz,elements);
    }

    private boolean determineRequiredStatus(Annotation ann) {
        Method method = ReflectionUtils.findMethod(ann.annotationType(),this.requiredParamterName);
        if (method == null)
            return true;
        return (Boolean)ReflectionUtils.invokeMethod(method,ann);
    }

    private Annotation findAutowiredAnnotation(AccessibleObject field) {
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes){
            Annotation ann = AnnotationUtils.getAnnotation(field,type);
            if (ann != null)
                return ann;
        }
        return null;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata injectionMetadata = buildAutowiringMetadata(bean.getClass());
        try{
            injectionMetadata.inject(bean);
        }
        catch (Throwable ex){
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);

        }
    }
}

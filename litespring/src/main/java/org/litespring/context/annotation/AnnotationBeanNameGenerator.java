package org.litespring.context.annotation;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.utils.ClassUtils;
import org.litespring.utils.StringUtils;

import java.beans.Introspector;
import java.util.Set;

/**
 * Created by zhengtengfei on 2018/8/9.
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegister registry) {
        if (definition instanceof AnnotatedBeanDefinition){
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)){
                return beanName;
            }
        }
        return buildDefaultBeanName(definition,registry);
    }

    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegister registry) {
        return buildDefaultBeanName(definition);
    }

    protected String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
        return Introspector.decapitalize(shortClassName);
    }

    private String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
        AnnotationMetadata amd = annotatedDef.getMetadata();
        Set<String> types = amd.getAnnotationTypes();
        String beanName = null;
        for (String type : types){
            AnnotationAttributes attributes = amd.getAnnotationAttributes(type);
            if (attributes.get("value") != null){
                Object value = attributes.get("value");
                if (value instanceof String){
                    String strVal = (String) value;
                    if (StringUtils.hasLength(strVal)){
                        beanName = strVal;
                    }
                }
            }
        }
        return beanName;
    }
}

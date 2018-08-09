package org.litespring.beans.factory.annotation;

import org.litespring.beans.BeanDefinition;
import org.litespring.core.type.AnnotationMetadata;

/**
 * Created by zhengtengfei on 2018/8/9.
 */
public interface AnnotatedBeanDefinition extends BeanDefinition{
    AnnotationMetadata getMetadata();
}

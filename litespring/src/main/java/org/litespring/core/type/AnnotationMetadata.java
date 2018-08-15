package org.litespring.core.type;

import org.litespring.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * Created by zhengtengfei on 2018/8/8.
 */
public interface AnnotationMetadata extends ClassMetadata {
    Set<String> getAnnotationTypes();

    boolean hasAnnotation(String annotationType);

    public AnnotationAttributes getAnnotationAttributes(String annotationType);
}

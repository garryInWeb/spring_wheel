package org.litespring.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by zhengtengfei on 2018/8/23.
 */
public class AnnotationUtils {

    public static <T extends Annotation> T getAnnotation(AnnotatedElement ae, Class<T> annotationType) {
        T ann = ae.getAnnotation(annotationType);
        if (ann == null) {
            for (Annotation metaAnn : ae.getAnnotations()) {
                ann = metaAnn.annotationType().getAnnotation(annotationType);
                if (ann != null) {
                    break;
                }
            }
        }
        return ann;
    }
}

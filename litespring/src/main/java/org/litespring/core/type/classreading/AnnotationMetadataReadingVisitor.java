package org.litespring.core.type.classreading;

import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.type.AnnotationMetadata;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhengtengfei on 2018/8/6.
 */
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata{
    private final Set<String> annotationSet = new LinkedHashSet<String>(4);
    private final Map<String,AnnotationAttributes> attributesMap = new LinkedHashMap<String,AnnotationAttributes>(4);

    public AnnotationMetadataReadingVisitor() {
    }
    @Override
    public AnnotationVisitor visitAnnotation(final String desc,boolean visiable){
        String className = Type.getType(desc).getClassName();
        this.annotationSet.add(className);
        return new AnnotationAttributesReadingVisitor(className,this.attributesMap);
    }

    public Set<String> getAnnotationTypes() {
        return this.annotationSet;
    }

    public boolean hasAnnotation(String annotation) {
        return this.annotationSet.contains(annotation);
    }

    public AnnotationAttributes getAnnotationAttributes(String annotation) {
        return this.attributesMap.get(annotation);
    }
}

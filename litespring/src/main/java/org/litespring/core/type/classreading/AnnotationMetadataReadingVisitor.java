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
 *
 */
public class  AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata{
    private final Set<String> annotationSet = new LinkedHashSet<String>(4);
    private final Map<String,AnnotationAttributes> attributesMap = new LinkedHashMap<String,AnnotationAttributes>(4);

    public AnnotationMetadataReadingVisitor() {
    }

    /**
     * 自定义 annotation 的读法
     * @param desc
     * @param visiable
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(final String desc,boolean visiable){
        // getType 相当于获得一个 Class
        String className = Type.getType(desc).getClassName();
        this.annotationSet.add(className);
        // 返回读取到的 annotationVisitor
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

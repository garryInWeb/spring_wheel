package org.litespring.core.type.classreading;


import org.litespring.core.annotation.AnnotationAttributes;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;

import java.util.Map;

/**
 * 相当于重写了 annotationVisitor
 */
final class AnnotationAttributesReadingVisitor extends AnnotationVisitor{

    // 保存需要对应的annotation类
    private final String annotationType;

    // 保存注解名以及其键值对
    private final Map<String,AnnotationAttributes> attributesMap;

    AnnotationAttributes attributes = new AnnotationAttributes();

    public AnnotationAttributesReadingVisitor(String annotationType,Map<String,AnnotationAttributes> attributesMap) {
        super(SpringAsmInfo.ASM_VERSION);
        this.annotationType = annotationType;
        this.attributesMap = attributesMap;
    }
    @Override
    public final void visitEnd(){
        this.attributesMap.put(this.annotationType, this.attributes);
    }

    /**
     * new 对象的时候会调用的方法，将对象 annotation 的key ，value 保存起来
     * @param attributeName
     * @param attributeValue
     */
    public void visit(String attributeName ,Object attributeValue){
        this.attributes.put(attributeName,attributeValue);
    }
}

package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ASM 读取文件 class 内容
 */
public class SimpleMetadataReader implements MetadataReader {

    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws IOException {
        InputStream in = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader;
        try{
            classReader = new ClassReader(in);
        }finally {
            in.close();
        }
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        // 调用visitor的方法，ASM 读取类信息
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.annotationMetadata = visitor;
        this.resource = resource;
        this.classMetadata = visitor;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }
}

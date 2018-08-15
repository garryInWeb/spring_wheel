package org.litespring.text.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.asm.ClassReader;

import java.io.IOException;

/**
 * Created by zhengtengfei on 2018/8/6.
 */
public class ClassReaderTest {
    @Test
    public void getAnnotation() throws IOException {
        ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();

        reader.accept(visitor,ClassReader.SKIP_DEBUG);

        String annotation = "org.litespring.stereotype.Component";
        Assert.assertTrue(visitor.hasAnnotation(annotation));

        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);

        Assert.assertEquals("petStore",attributes.get("value"));
    }
}

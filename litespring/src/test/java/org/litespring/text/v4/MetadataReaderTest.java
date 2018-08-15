package org.litespring.text.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;

import java.io.IOException;

/**
 * Created by zhengtengfei on 2018/8/8.
 */
public class MetadataReaderTest {
    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");

        MetadataReader metadataReader = new SimpleMetadataReader(classPathResource);

        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
        AnnotationAttributes annotationAttributes = annotationMetadata.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore",annotationAttributes.get("value"));

        Assert.assertFalse(annotationMetadata.isFinal());
        Assert.assertEquals("org.litespring.service.v4.PetStoreService",annotationMetadata.getClassName());


    }
}

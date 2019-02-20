package org.litespring.context.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;
import org.litespring.utils.StringUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 提供扫描包并注册到factory的方法
 */
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegister register;

    private PackageResourceLoader resourceLoader = new PackageResourceLoader();

    protected final Log logger = LogFactory.getLog(getClass());

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegister register) {
        this.register = register;
    }

    /**
     * 扫描包下带Component注解的类，注册到beanFactory中
     * @param packageToScan
     * @return
     */
    public Set<BeanDefinition> doScan(String packageToScan){
        String[] basePackages = StringUtils.tokenizeToStringArray(packageToScan,",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();

        for (String basepackage : basePackages){
            Set<BeanDefinition> candidatas = findCandidatasComponent(basepackage);
            for (BeanDefinition bd : candidatas){
                beanDefinitions.add(bd);
                register.registerBeanDefinition(bd);
            }
        }
        return beanDefinitions;
    }

    /**
     * 扫描包下所有带Component注解的类包装成BeanDefinition返回
     * @param basepackage
     * @return
     */
    private Set<BeanDefinition> findCandidatasComponent(String basepackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            // 获取包名下所有文件resource
            Resource[] resources = this.resourceLoader.getResources(basepackage);

            for (Resource resource : resources) {
                try {
                    // ASM 解析
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        // 注解的抽象类
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        // 获取注解的value作为bean对象的id
                        String beanName = this.beanNameGenerator.generateBeanName(sbd);
                        sbd.setId(beanName);
                        candidates.add(sbd);
                    }
                }catch (Throwable ex){
                    throw new BeanDefinitionStoreException("Faild to read candidate component class:" + resource,ex);
                }
            }
        }catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }
}

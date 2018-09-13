package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.aop.config.ConfigBeanDefinitionParser;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 对配置文件进行解析和加载factory的类
 */
public class XmlBeanDefinitionReader {

    private static final Log log = LogFactory.getLog(XmlBeanDefinitionReader.class);
    public static final String REF_ATTRIBUTE = "ref";
    private static final String IDENTITY = "id";
    private static final String CLASS_NAME = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String NAME = "name";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String CONSTRUCTOR_ARG = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String NAME_ATTRIBUTE = "name";

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    BeanDefinitionRegister beanDefinitionRegister;

    public XmlBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        this.beanDefinitionRegister = beanDefinitionRegister;
    }

    /**
     * 读取配置信息，加载到BeanDefinitionRegister中
     * @param resource 配置文件的resource
     */
    public void loadBeanDefinitions(Resource resource) {
        InputStream in = null;
        try{
            in = resource.getInputStream();

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);

            Element root = document.getRootElement();
            Iterator<Element> elementIterator = root.elementIterator();
            while (elementIterator.hasNext()){
                Element el = elementIterator.next();
                // 获取命名空间来区分 bean 和 component-scan
                String namespaceUri = el.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)){
                    parseDefaultElement(el);
                }else if (this.isContextNamespace(namespaceUri)){
                    parseContextElement(el);
                }else if (this.isAopNamespace(namespaceUri)){
                    parseAopElement(el);
                }

            }
        }catch (Exception e){
            // TODO 这个e抛得。。。
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(),e);
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.fatal("InputStream close fail , because of " + e);
                }
            }
        }

    }

    private void parseAopElement(Element el) {
        ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
        parser.parse(el,this.beanDefinitionRegister);
    }

    /**
     * 扫描包名下所有带注解的类，注册到beanFactory中
     * @param el
     */
    private void parseContextElement(Element el) {
        String basePackages = el.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegister);
        scanner.doScan(basePackages);
    }

    /**
     * 根据配置信息进行 bean 构造
     * @param el
     */
    private void parseDefaultElement(Element el) {
        String id = el.attributeValue(IDENTITY);
        String className = el.attributeValue(CLASS_NAME);
        BeanDefinition beanDefinition = new GenericBeanDefinition(id,className);
        if (el.attribute(SCOPE_ATTRIBUTE) != null){
            beanDefinition.setScope(el.attributeValue(SCOPE_ATTRIBUTE));
        }
        // 配置property信息
        parsePropertyElement(el,beanDefinition);
        // 配置constructor-arg信息
        parseConstructor(el,beanDefinition);
        // 添加 beanDefinition 到 factory 中
        beanDefinitionRegister.registerBeanDefinition(beanDefinition);
    }

    /**
     * 判断是否为 context 的命名空间
     * @param namespaceUri
     * @return
     */
    private boolean isContextNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    /**
     * 判断是否为 bean 的命名空间
     * @param namespaceUri
     * @return
     */
    private boolean isDefaultNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    /**
     * 判断是否为 AOP 的命名空间
     * @param namespaceUri
     * @return
     */
    private boolean isAopNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && AOP_NAMESPACE_URI.equals(namespaceUri));

    }


    /**
     * 获取配置文件中 bean 下的 constructor-arg 信息
     * @param el 配置文件中bean节点
     * @param beanDefinition 返回的 bean 定义
     */
    private void parseConstructor(Element el, BeanDefinition beanDefinition) {
        Iterator<Element> constructorElement = el.elementIterator(CONSTRUCTOR_ARG);
        while(constructorElement.hasNext()){
            Element ele = constructorElement.next();
            parseConstructorArgElement(ele,beanDefinition);
        }
    }

    /**
     * 对 constructor-arg 信息进行抽象，保存到 beanDefinition 中
     * @param ele constructor-arg 节点
     * @param beanDefinition 返回的 bean 定义
     */
    private void parseConstructorArgElement(Element ele, BeanDefinition beanDefinition) {
        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        // constructor-arg的值抽象为对象
        Object value = parsePropertyValue(ele,null);
        // 将抽象对象保存到beanDefinition的字段中
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)){
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)){
            valueHolder.setName(nameAttr);
        }
        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);
    }

    /**
     * 获取配置文件中 bean 下的 property 信息
     * @param el 配置文件中bean节点
     * @param bd 返回的 bean 定义
     */
    private void parsePropertyElement(Element el,BeanDefinition bd) {
        Iterator<Element> propertyElement = el.elementIterator(PROPERTY_ELEMENT);
        while (propertyElement.hasNext()){
            Element property = propertyElement.next();
            String propertyName = property.attributeValue(NAME);
            if (!StringUtils.hasLength(propertyName)){
                log.fatal("Tag 'property' must have a 'name' attribute .");
                return;
            }
            Object value = parsePropertyValue(property, property.attributeValue(NAME));
            // 抽象为PropertyValue类，添加到beanDefinition定义的字段中
            PropertyValue propertyValue = new PropertyValue(value,propertyName);
            bd.getPropertyValues().add(propertyValue);
        }
    }

    /**
     * 读取 bean 的属性值，抽象为RuntimeBeanReference或者TypedStringValue类，分别对应 ref 类和 value 值
     * @param property bean 下的 property 节点
     * @param beanName
     * @return
     */
    private Object parsePropertyValue(Element property,String beanName) {

        String elementName = (beanName != null)?
                            "<property> element for property '" + beanName + "'":
                            "<constructor-arg> element";

        boolean hasRefAttribute = property.attribute(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = property.attribute(VALUE_ATTRIBUTE) != null;
        // 关联型属性
        if (hasRefAttribute) {
            String refName = property.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)){
                log.fatal(elementName + " contains a empty 'ref' attribute");
            }
            return new RuntimeBeanReference(refName);
        // 键值型属性
        }else if (hasValueAttribute){
            return new TypedStringValue(property.attributeValue(VALUE_ATTRIBUTE));
        }else{
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }
}

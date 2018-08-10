package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    BeanDefinitionRegister beanDefinitionRegister;

    public XmlBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        this.beanDefinitionRegister = beanDefinitionRegister;
    }

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
                String namespaceUri = el.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)){
                    parseDefaultElement(el);
                }else if (this.isContextNamespace(namespaceUri)){
                    parseContextElement(el);
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

    private void parseContextElement(Element el) {
        String basePackages = el.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegister);
        scanner.doScan(basePackages);
    }

    private void parseDefaultElement(Element el) {
        String id = el.attributeValue(IDENTITY);
        String className = el.attributeValue(CLASS_NAME);
        BeanDefinition beanDefinition = new GenericBeanDefinition(id,className);
        if (el.attribute(SCOPE_ATTRIBUTE) != null){
            beanDefinition.setScope(el.attributeValue(SCOPE_ATTRIBUTE));
        }
        parsePropertyElement(el,beanDefinition);
        parseConstructor(el,beanDefinition);
        beanDefinitionRegister.registerBeanDefinition(beanDefinition);
    }

    private boolean isContextNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    private boolean isDefaultNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    private void parseConstructor(Element el, BeanDefinition beanDefinition) {
        Iterator<Element> constructorElement = el.elementIterator(CONSTRUCTOR_ARG);
        while(constructorElement.hasNext()){
            Element ele = constructorElement.next();
            parseConstructorArgElement(ele,beanDefinition);
        }
    }

    private void parseConstructorArgElement(Element ele, BeanDefinition beanDefinition) {
        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(ele,null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)){
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)){
            valueHolder.setName(nameAttr);
        }
        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);
    }

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
            PropertyValue propertyValue = new PropertyValue(value,propertyName);
            bd.getPropertyValues().add(propertyValue);
        }
    }

    private Object parsePropertyValue(Element property,String beanName) {

        String elementName = (beanName != null)?
                            "<property> element for property '" + beanName + "'":
                            "<constructor-arg> element";

        boolean hasRefAttribute = property.attribute(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = property.attribute(VALUE_ATTRIBUTE) != null;
        if (hasRefAttribute) {
            String refName = property.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)){
                log.fatal(elementName + " contains a empty 'ref' attribute");
            }
            return new RuntimeBeanReference(refName);
        }else if (hasValueAttribute){
            return new TypedStringValue(property.attributeValue(VALUE_ATTRIBUTE));
        }else{
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }
}

package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.apache.commons.logging.LogFactory;

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
                Element el = (Element)elementIterator.next();
                String id = el.attributeValue(IDENTITY);
                String className = el.attributeValue(CLASS_NAME);
                BeanDefinition beanDefinition = new GenericBeanDefinition(id,className);
                if (el.attribute(SCOPE_ATTRIBUTE) != null){
                    beanDefinition.setScope(el.attributeValue(SCOPE_ATTRIBUTE));
                }
                beanDefinitionRegister.registerBeanDefinition(beanDefinition);
                parsePropertyElement(el,beanDefinition);
            }
        }catch (Exception e){
            // TODO 这个e抛得。。。
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(),e);
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void parsePropertyElement(Element el,BeanDefinition bd) {
        Iterator<Element> propertyElement = el.elementIterator(PROPERTY_ELEMENT);
        while (propertyElement.hasNext()){
            Element property = (Element)propertyElement.next();
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
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        }else if (hasValueAttribute){
            TypedStringValue valueHolder = new TypedStringValue(property.attributeValue(VALUE_ATTRIBUTE));
            return valueHolder;
        }else{
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }
}

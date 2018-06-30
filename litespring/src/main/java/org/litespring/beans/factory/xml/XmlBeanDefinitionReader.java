package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    private static final String IDENTITY = "id";
    private static final String CLASS_NAME = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";

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
}

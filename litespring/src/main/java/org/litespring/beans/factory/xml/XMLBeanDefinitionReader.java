package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XMLBeanDefinitionReader{

    private static final String IDENTITY = "id";
    private static final String CLASS_NAME = "class";

    BeanDefinitionRegister beanDefinitionRegister;

    public XMLBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        this.beanDefinitionRegister = beanDefinitionRegister;
    }

    public void loadBeanDefinitions(String configFile) {
        InputStream in = null;
        try{
            ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
            in = classLoader.getResourceAsStream(configFile);

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);

            Element root = document.getRootElement();
            Iterator<Element> elementIterator = root.elementIterator();
            while (elementIterator.hasNext()){
                Element el = (Element)elementIterator.next();
                String id = el.attributeValue(IDENTITY);
                String className = el.attributeValue(CLASS_NAME);
                BeanDefinition beanDefinition = new GenericBeanDefinition(id,className);
                beanDefinitionRegister.registerBeanDefinition(beanDefinition);
            }
        }catch (DocumentException e){
            throw new BeanDefinitionStoreException("File "+ configFile +"is not exist.");
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

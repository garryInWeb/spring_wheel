package org.litespring.text.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;

import java.util.List;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class BeanDefinitionTestV2 {

    @Test
    public void testBeanDefinition(){
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));
        BeanDefinition bd = defaultBeanFactory.getBeanDefinition("petStore");

        List<PropertyValue> propertyValueList = bd.getPropertyValues();

        Assert.assertTrue(propertyValueList.size() == 4);
        {
            PropertyValue pv = this.getPropertyValue("accountDao",propertyValueList);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }
        {
            PropertyValue pv = this.getPropertyValue("itemDao",propertyValueList);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }


    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> propertyValueList) {
        for (PropertyValue pv : propertyValueList){
            if (pv.getName().equals(name))
                return pv;
        }
        return null;
    }
}

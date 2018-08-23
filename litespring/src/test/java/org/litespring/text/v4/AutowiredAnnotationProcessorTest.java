package org.litespring.text.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.annotation.AutowiredFieldElement;
import org.litespring.beans.factory.annotation.InjectionElement;
import org.litespring.beans.factory.annotation.InjectionMetadata;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.dao.v4.AccountDao;
import org.litespring.dao.v4.ItemDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/8/22.
 */
public class AutowiredAnnotationProcessorTest {

    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();
    DefaultBeanFactory factory = new DefaultBeanFactory(){
        @Override
        public Object resolveDenpendency(DependencyDescriptor descriptor){
            if(descriptor.getDenpendencyType().equals(AccountDao.class)){
                return accountDao;
            }
            if(descriptor.getDenpendencyType().equals(ItemDao.class)){
                return itemDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };

    @Test
    public void testGetInjectionMetadata(){
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(factory);
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> elements = injectionMetadata.getElements();
        Assert.assertEquals(2,elements.size());

        assertFieldExist(elements,"accountDao");
        assertFieldExist(elements,"itemDao");

        PetStoreService petStoreService = new PetStoreService();

        injectionMetadata.inject(petStoreService);

        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);
        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
    }

    private void assertFieldExist(List<InjectionElement> elements, String fieldName) {
        for (InjectionElement element : elements){
            AutowiredFieldElement fieldElement = (AutowiredFieldElement) element;
            Field f = fieldElement.getField();
            if (f.getName().equals(fieldName)){
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }


}

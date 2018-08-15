package org.litespring.text.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.TypeMismatchException;

import static org.junit.Assert.fail;

/**
 * Created by zhengtengfei on 2018/7/6.
 */
public class TypeConverterTest {
    @Test
    public void testConverterString2Int() throws TypeMismatchException {
        TypeConverter converter = new SimpleTypeConverter();
        Integer i = converter.converterIfNecessary("3",Integer.class);
        Assert.assertEquals(3,i.intValue());
        try{
            converter.converterIfNecessary("3.1",Integer.class);
        }catch (TypeMismatchException e){
            return;
        }
        fail();
    }
}

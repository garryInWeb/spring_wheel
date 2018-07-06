package org.litespring.text.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.propertyeditors.CustomNumberEditor;

/**
 * Created by zhengtengfei on 2018/7/2.
 */
public class CustomNumberEditorTest {
    @Test
    public void testConverString(){
        CustomNumberEditor editor = new CustomNumberEditor(Integer.class,true);

        editor.setAsText("3");
        Object value = editor.getValue();
        Assert.assertTrue(value instanceof Integer);
        Assert.assertTrue((Integer) value == 3);

        editor.setAsText("");
        Assert.assertNull(editor.getValue());

        try{
            editor.setAsText("3.1");
        }catch (Exception e){
            return;
        }
        Assert.fail();
    }
}

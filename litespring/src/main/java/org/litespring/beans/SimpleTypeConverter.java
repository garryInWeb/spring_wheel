package org.litespring.beans;

import org.litespring.beans.propertyeditors.CustomBooleanEditor;
import org.litespring.beans.propertyeditors.CustomNumberEditor;
import org.litespring.utils.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengtengfei on 2018/7/6.
 */
public class SimpleTypeConverter implements TypeConverter {

    private Map<Class<?>,PropertyEditor> defaultEditor;

    public SimpleTypeConverter() {
    }

    @Override
    public <T> T converterIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        // 校验是否为基本类型和对象类型
        if (ClassUtils.isAssignableValue(requiredType,value)){
            return (T)value;
        } else{
            if (value instanceof String){
                PropertyEditor propertyEditor = findDefaultEditor(requiredType);
                try{
                    propertyEditor.setAsText((String) value);
                }catch (IllegalArgumentException e){
                    throw new TypeMismatchException(value,requiredType);
                }
                return (T)propertyEditor.getValue();
            } else{
                throw new RuntimeException("Todo : can't convert value for "+value +" class:"+requiredType);
            }
        }
    }

    private PropertyEditor findDefaultEditor(Class<?> requiredType){
        PropertyEditor propertyEditor = this.getDefaultEditor(requiredType);
        if (propertyEditor == null){
            throw new RuntimeException("Editor for "+requiredType+" has not been implements.");
        }
        return propertyEditor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType){
        if (this.defaultEditor == null){
            createDefaultEditor();
        }
        return this.defaultEditor.get(requiredType);
    }

    private void createDefaultEditor() {
        this.defaultEditor = new HashMap<Class<?>,PropertyEditor>();

        this.defaultEditor.put(boolean.class,new CustomBooleanEditor(false));
        this.defaultEditor.put(Boolean.class,new CustomBooleanEditor(true));

        this.defaultEditor.put(int.class,new CustomNumberEditor(Integer.class,false));
        this.defaultEditor.put(Integer.class,new CustomNumberEditor(Integer.class,true));
    }
}

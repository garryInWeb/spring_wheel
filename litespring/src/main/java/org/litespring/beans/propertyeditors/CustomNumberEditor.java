package org.litespring.beans.propertyeditors;

import org.litespring.utils.NumberUtil;
import org.litespring.utils.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

/**
 * Created by zhengtengfei on 2018/7/2.
 */
public class CustomNumberEditor extends PropertyEditorSupport{

    private final Class<? extends Number> numberClass;

    private final NumberFormat numberFormat;

    private final boolean allowNull;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowNull) {
        this(numberClass,null,allowNull);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowNull) {

        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)){
            throw new IllegalArgumentException("Property class must be a subclass of Number.");
        }
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowNull = allowNull;
    }

    @Override
    public void setAsText(String text){
        if (this.allowNull && !StringUtils.hasText(text)){
            setValue(null);
        }else if(this.numberFormat != null){
            setValue(NumberUtil.parseNumber(text,this.numberClass,this.numberFormat));
        }else{
            setValue(NumberUtil.parseNumber(text,this.numberClass));
        }
    }
    @Override
    public void setValue(Object value){
        if (value instanceof Number){
            super.setValue(NumberUtil.convertNumberToTargetClass((Number)value,this.numberClass));
        }else{
            super.setValue(value);
        }
    }

    @Override
    public String getAsText(){
        Object value = getValue();
        if (value == null){
            return null;
        }
        if (this.numberFormat != null){
            return this.numberFormat.format(value);
        }else{
            return value.toString();
        }
    }

}

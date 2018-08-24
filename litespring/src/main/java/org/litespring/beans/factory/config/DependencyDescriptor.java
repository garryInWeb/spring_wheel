package org.litespring.beans.factory.config;

import org.litespring.utils.Assert;

import java.lang.reflect.Field;

/**
 *
 */
public class DependencyDescriptor {
    private Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field,"Field must not be null!");
        this.field = field;
        this.required = required;
    }

    public Class<?> getDenpendencyType(){
        if (this.field != null) {
            return field.getType();
        }
        throw new RuntimeException("only support field denpendency");
    }

    public boolean isRequired() {
        return required;
    }
}

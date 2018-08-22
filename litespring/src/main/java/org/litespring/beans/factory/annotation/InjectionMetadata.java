package org.litespring.beans.factory.annotation;

import java.util.LinkedList;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
public class InjectionMetadata {


    private final Class<?> clazz;
    private LinkedList<InjectionElement> elements;

    public InjectionMetadata(Class<?> clazz, LinkedList<InjectionElement> elements) {
        this.clazz = clazz;
        this.elements = elements;
    }

    public void inject(Object target) {
        if (elements == null || elements.isEmpty()){
            return;
        }
        for (InjectionElement element : elements){
            element.inject(target);
        }
    }
}

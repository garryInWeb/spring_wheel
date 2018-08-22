package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

/**
 * Created by zhengtengfei on 2018/8/21.
 */
public abstract class InjectionElement {
    protected Member member;
    protected AutowireCapableBeanFactory factory;

    public InjectionElement(Member member, AutowireCapableBeanFactory factory) {
        this.member = member;
        this.factory = factory;
    }
    public abstract void inject(Object target);
}

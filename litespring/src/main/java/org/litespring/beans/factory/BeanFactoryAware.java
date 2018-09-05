package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * Created by zhengtengfei on 2018/9/5.
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory factory)throws BeansException;
}

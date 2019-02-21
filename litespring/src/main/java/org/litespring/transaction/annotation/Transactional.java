package org.litespring.transaction.annotation;

import java.lang.annotation.*;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transactional {
}

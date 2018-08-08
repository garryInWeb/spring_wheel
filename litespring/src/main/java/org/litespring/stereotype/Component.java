package org.litespring.stereotype;

import java.lang.annotation.*;

/**
 * Created by zhengtengfei on 2018/7/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}

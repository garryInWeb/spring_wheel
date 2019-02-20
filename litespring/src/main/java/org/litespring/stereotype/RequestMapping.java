package org.litespring.stereotype;

import java.lang.annotation.*;

/**
 * Created by zhengtengfei on 2018/10/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}

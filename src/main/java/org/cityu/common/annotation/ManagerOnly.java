package org.cityu.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagerOnly {
    String message() default "only used by merchant";
}

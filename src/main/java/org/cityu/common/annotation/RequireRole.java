package org.cityu.common.annotation;

import java.lang.annotation.*;

// 基于角色的访问控制注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    String[] value() default {};
    String message() default "";
}
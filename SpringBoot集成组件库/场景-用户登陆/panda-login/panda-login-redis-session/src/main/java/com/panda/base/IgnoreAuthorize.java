package com.panda.base;

import java.lang.annotation.*;

/**
 * 忽略授权
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuthorize {
}

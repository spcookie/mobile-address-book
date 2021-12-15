package com.cqut.addressBook.annotation;

import java.lang.annotation.*;

/**
 * @author Augenstern
 * @date 2021/12/6
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    String name() default "";
}

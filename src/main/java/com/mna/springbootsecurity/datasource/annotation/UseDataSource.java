package com.mna.springbootsecurity.datasource.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseDataSource {
    String value(); // Example: "ds-mysql-school"
}


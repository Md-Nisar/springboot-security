package com.mna.springbootsecurity.distributed.lock.annotation;

import com.mna.springbootsecurity.distributed.base.enums.LockKey;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    LockKey lockKey();

    long waitTime() default 0;    // Max time to wait for acquiring lock

    long leaseTime() default 120;   // Max lock holding time

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}


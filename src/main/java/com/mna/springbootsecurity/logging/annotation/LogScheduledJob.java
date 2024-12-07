package com.mna.springbootsecurity.logging.annotation;

import com.mna.springbootsecurity.logging.enums.ScheduledJobName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogScheduledJob {
    ScheduledJobName jobName();
}

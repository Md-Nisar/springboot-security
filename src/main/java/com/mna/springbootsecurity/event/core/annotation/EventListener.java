package com.mna.springbootsecurity.event.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Custom annotation to mark classes as event handlers.
 *
 * Works in conjunction with auto-registration logic that scans for and registers
 * all event handlers at startup.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EventListener {
}


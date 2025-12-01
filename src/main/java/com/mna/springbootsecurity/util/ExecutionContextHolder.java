package com.mna.springbootsecurity.util;

import java.util.EnumMap;

public enum ExecutionContextHolder {
    USER,              // Stores the authenticated user object
    ROLE,              // Stores user roles
    REQUEST_ID,        // Stores a unique request ID for tracing logs
    TENANT_ID,         // Stores multi-tenant system identifier
    SESSION_ID,        // Stores session identifier
    AUTH_TOKEN,        // Stores authentication token (JWT or session token)
    LOCALE,            // Stores user locale/language preferences
    CLIENT_IP,         // Stores client IP address
    USER_AGENT,        // Stores HTTP User-Agent header
    START_TIME,        // Stores request start time for performance tracking
    ADDITIONAL_DATA,   // Stores any other additional data
    TASK_LOGGER,
    MESSAGES

    ;

    private static final ThreadLocal<EnumMap<ExecutionContextHolder, Object>> THREAD_LOCAL = ThreadLocal.withInitial(() -> new EnumMap<>(ExecutionContextHolder.class));

    public static void put(ExecutionContextHolder key, Object value) {
        EnumMap<ExecutionContextHolder, Object> context = THREAD_LOCAL.get();
        context.put(key, value);
    }

    public static Object get(ExecutionContextHolder key) {
        EnumMap<ExecutionContextHolder, Object> context = THREAD_LOCAL.get();
        return context.get(key);
    }

    public static void remove(ExecutionContextHolder key) {
        EnumMap<ExecutionContextHolder, Object> context = THREAD_LOCAL.get();
        context.remove(key);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}


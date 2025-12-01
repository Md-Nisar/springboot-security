package com.mna.springbootsecurity.datasource.context;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(String datasourceKey) {
        log.info(">> Switching to DataSource: {}", datasourceKey);
        CONTEXT.set(datasourceKey);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        log.info("Clearing DataSource context");
        CONTEXT.remove();
    }
}

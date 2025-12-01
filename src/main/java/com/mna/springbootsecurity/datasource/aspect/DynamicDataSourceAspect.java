package com.mna.springbootsecurity.datasource.aspect;

import com.mna.springbootsecurity.datasource.annotation.UseDataSource;
import com.mna.springbootsecurity.datasource.context.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1) // Ensures this runs before @Transactional (default 0)
@Slf4j
public class DynamicDataSourceAspect {

    @Around("@annotation(useDataSource)")
    public Object switchDataSource(ProceedingJoinPoint joinPoint, UseDataSource useDataSource) throws Throwable {
        String key = useDataSource.value();
        DataSourceContextHolder.set(key);
        try {
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
    }
}


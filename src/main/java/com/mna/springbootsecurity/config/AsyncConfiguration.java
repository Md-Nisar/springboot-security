package com.mna.springbootsecurity.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@ConditionalOnProperty(name = "application.services.async.enabled", havingValue = "true")
@Getter
@Slf4j
public class AsyncConfiguration {

    @Value("${application.services.async.enabled}")
    private boolean asyncServiceEnabled;

    @PostConstruct
    public void init() {
        log.info("Async execution is enabled: {}", asyncServiceEnabled);
    }

    /**
     * Configures a ThreadPoolTaskExecutor for handling @Async tasks efficiently.
     */
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        log.info("Initializing Async Executor with core pool size: 5, max pool size: 10");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Async-Task-");
        executor.initialize(); // Ensures proper initialization

        log.info("Async Executor initialized successfully.");
        return executor;
    }
}

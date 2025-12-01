package com.mna.springbootsecurity.scheduling.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "application.services.scheduling.enabled", havingValue = "true")
@Getter
@Slf4j
public class SchedulingConfiguration {

    @Value("${application.services.scheduling.enabled}")
    private boolean schedulingServiceEnabled;

    @PostConstruct
    public void init() {
        log.info("Scheduling is enabled: {}", schedulingServiceEnabled);
    }

    /**
     * Custom ThreadPoolTaskScheduler to handle scheduled tasks with a pool of 5 threads.
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        log.info("Initializing Task Scheduler with Pool Size: 5");

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("Scheduler-Task-");
        scheduler.initialize(); // Ensure proper initialization

        log.info("Task Scheduler initialized successfully.");
        return scheduler;
    }
}

package com.mna.springbootsecurity.scheduling.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

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
        log.info("Scheduling enabled");
    }


}

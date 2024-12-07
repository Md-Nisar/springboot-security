package com.mna.springbootsecurity.pubsub.event.application.subscriber;

import com.mna.springbootsecurity.pubsub.event.application.util.PropertiesLogger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * A class that listens to important Spring Boot application events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEventListener {

    private final PropertiesLogger logger;
    private boolean isFirstRun = true;

    @EventListener
    public void handleApplicationReadyEvent(@NonNull ApplicationReadyEvent event) {
        if (isFirstRun) {
            log.info("Application Ready Event Triggered");
            // logger.logEnvironmentProperties();
            // logger.logSystemProperties();
            // logger.logApplicationProperties();
            // logger.logMemoryUsage();
            isFirstRun = false;
        } else {
            log.info("Application Ready Event (Subsequent Run)");
        }
    }

}

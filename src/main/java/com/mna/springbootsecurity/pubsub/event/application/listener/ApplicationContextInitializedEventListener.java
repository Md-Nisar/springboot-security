package com.mna.springbootsecurity.pubsub.event.application.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationContextInitializedEventListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationContextInitializedEvent event) {
        log.info(">>> Application Context Initialized Triggered!");
    }
}

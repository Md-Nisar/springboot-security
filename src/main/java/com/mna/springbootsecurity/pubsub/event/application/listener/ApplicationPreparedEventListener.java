package com.mna.springbootsecurity.pubsub.event.application.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationPreparedEvent event) {
        log.info(">>> Application Prepared Event Triggered!");

    }

}

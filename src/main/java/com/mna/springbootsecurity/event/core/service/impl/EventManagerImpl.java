package com.mna.springbootsecurity.event.core.service.impl;

import com.mna.springbootsecurity.event.core.model.Event;
import com.mna.springbootsecurity.event.core.publisher.EventPublisher;
import com.mna.springbootsecurity.event.core.service.EventManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventManagerImpl implements EventManager {

    @Autowired(required = false)
    private EventPublisher<Event> eventPublisher;

    @Override
    public void triggerEvent(Event event) {
        // Fetch event data (event variables) from DB by EventType
        // Use Event utility class to create variable data (from payload set)
        // Prepare event for publishing by setting a trace id, context data etc
        eventPublisher.publish(event);
    }
}

package com.mna.springbootsecurity.event.core.model;


import com.mna.springbootsecurity.event.common.enums.EventType;

import java.time.Instant;

/**
 * Represents an event in the system.
 * All events should implement this interface to ensure consistency.
 * Examples include: UserRegisteredEvent, OrderPlacedEvent, etc.
 */
public interface Event {

    /**
     * Retrieves the unique identifier of the event.
     *
     * @return the event's unique ID.
     */
    String getEventId();

    /**
     * Retrieves the type of the event.
     *
     * @return the event's type as an {@link EventType}.
     */
    EventType getEventType();

    /**
     * Retrieves the timestamp indicating when the event occurred.
     *
     * @return the event's occurrence time.
     */
    Instant getOccurredAt();
}


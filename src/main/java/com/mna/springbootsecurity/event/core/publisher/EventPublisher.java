package com.mna.springbootsecurity.event.core.publisher;

/**
 * Interface for publishing domain events.
 *
 * Implementations may publish events using in-memory dispatch,
 * messaging systems like RabbitMQ, Kafka, or other broker systems.
 */
import com.mna.springbootsecurity.event.core.model.Event;

public interface EventPublisher<T> {

    /**
     * Publishes the given domain event.
     *
     * @param event the event to publish
     */
    void publish(T event);

}


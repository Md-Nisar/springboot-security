package com.mna.springbootsecurity.event.core.listener;

/**
 * Generic interface to listen events of type T.
 * <p>
 * Implement this interface to define specific logic for handling an event.
 *
 * @param <T> the type of the Event to handle
 */
public interface EventListener<T> {

    /**
     * Handles the given event.
     *
     * @param event the domain event to handle
     */
    void consume(T event);

}


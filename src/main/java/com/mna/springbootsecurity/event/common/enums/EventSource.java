package com.mna.springbootsecurity.event.common.enums;

/**
 * Enum representing the origin of an event.
 */
public enum EventSource {
    /**
     * Event originated from the system itself.
     */
    SYSTEM,

    /**
     * Event originated from an external source.
     */
    EXTERNAL,

    /**
     * Event originated from a user action.
     */
    USER
}


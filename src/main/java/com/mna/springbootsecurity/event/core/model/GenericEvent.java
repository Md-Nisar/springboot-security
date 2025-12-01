package com.mna.springbootsecurity.event.core.model;

import com.mna.springbootsecurity.event.common.enums.EventType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A generic implementation of the {@link Event} interface that can carry any payload.
 *
 * @param <T> the type of the payload.
 */
@Getter
public final class GenericEvent<T> implements Event, Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Ensures compatibility during deserialization

    private final String eventId;
    private final EventType eventType;
    private final Instant occurredAt;
    private final T payload;
    private final EventMetadata metadata;
    private final String version;

    private GenericEvent(Builder<T> builder) {
        this.eventId = builder.eventId;
        this.eventType = Objects.requireNonNull(builder.eventType, "EventType must not be null");
        this.occurredAt = builder.occurredAt != null ? builder.occurredAt : Instant.now();
        this.payload = Objects.requireNonNull(builder.payload, "Payload must not be null");
        this.metadata = builder.metadata != null ? builder.metadata : new EventMetadata(null, null, null, null);
        this.version = builder.version != null ? builder.version : "1.0";
    }

    public static class Builder<T> {
        private final String eventId = UUID.randomUUID().toString();
        private EventType eventType;
        private Instant occurredAt;
        private T payload;
        private EventMetadata metadata;
        private String version = "1.0";

        public Builder<T> eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder<T> occurredAt(Instant occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

        public Builder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public Builder<T> metadata(EventMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder<T> version(String version) {
            this.version = version;
            return this;
        }

        public GenericEvent<T> build() {
            return new GenericEvent<>(this);
        }
    }
}

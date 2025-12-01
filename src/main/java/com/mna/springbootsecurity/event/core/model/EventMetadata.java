package com.mna.springbootsecurity.event.core.model;

import lombok.Getter;

import java.util.Map;

/**
 * Contains additional contextual information about an event, such as tracing identifiers and source details.
 */
@Getter
public class EventMetadata {


    private final String traceId;
    private final String source;
    private final String correlationId;
    private final Map<String, String> customData;

    /**
     * Constructs an EventMetadata instance with the specified parameters.
     *
     * @param traceId       the trace identifier for distributed tracing.
     * @param source        the origin of the event.
     * @param correlationId the identifier used to correlate related events.
     * @param customData    any additional custom metadata.
     */
    public EventMetadata(String traceId, String source, String correlationId, Map<String, String> customData) {
        this.traceId = traceId;
        this.source = source;
        this.correlationId = correlationId;
        this.customData = customData != null ? customData : Map.of();
    }

}

package com.mna.springbootsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Jackson ObjectMapper.
 * This configuration ensures proper serialization and deserialization of Java 8 Date and Time API types.
 * - Registers the JavaTimeModule to handle Java 8 Date and Time types (e.g., LocalDate, LocalDateTime).
 * - Configures the ObjectMapper to serialize dates as ISO-8601 formatted strings instead of timestamps.
 */
@Configuration
@Slf4j
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Ensure dates are serialized as ISO-8601
        log.info("Jackson 'ObjectMapper' configured successfully!");
        return objectMapper;
    }

}

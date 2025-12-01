package com.mna.springbootsecurity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JSONUtil {

    private final ObjectMapper objectMapper;

    /**
     * Converts a Java object to a JSON string.
     */
    public String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON string: {}", obj, e);
            return null;
        }
    }

    /**
     * Converts a JSON string to a Java object.
     */
    public <T> T fromJsonString(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON string to object: {}", jsonString, e);
            return null;
        }
    }

    /**
     * Converts a JSON string to a List of Java objects.
     */
    public <T> List<T> fromJsonStringToList(String jsonString, TypeReference<List<T>> typeRef) {
        try {
            return objectMapper.readValue(jsonString, typeRef);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON string to list: {}", jsonString, e);
            return null;
        }
    }

    /**
     * Parses a JSON string into a JsonNode for further manipulation.
     */
    public JsonNode parseToJsonNode(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON string to JsonNode: {}", jsonString, e);
            return null;
        }
    }

    /**
     * Pretty-prints a JSON string.
     */
    public String prettyPrint(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("Error pretty-printing JSON string: {}", jsonString, e);
            return jsonString;
        }
    }

    /**
     * Checks if a string is a valid JSON.
     */
    public boolean isValidJson(String jsonString) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}

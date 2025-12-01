package com.mna.springbootsecurity.util;

import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    private ResponseUtil() {
    }

    public static <T> ResponseEntity<ControllerResponse<T>> success(T data) {
        ControllerResponse<T> response = ControllerResponse.<T>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(true)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ControllerResponse<Void>> success(String message) {
        ControllerResponse<Void> response = ControllerResponse.<Void>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(true)
                .message(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ControllerResponse<T>> success(T data, String message) {
        ControllerResponse<T> response = ControllerResponse.<T>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(true)
                .data(data)
                .message(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ControllerResponse<T>> error(T data, HttpStatus status, List<String> errors) {
        ControllerResponse<T> response = ControllerResponse.<T>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(false)
                .data(data)
                .message(status.getReasonPhrase())
                .errors(errors)
                .build();
        return ResponseEntity.status(status.value()).body(response);
    }

    public static ResponseEntity<ControllerResponse<Void>> error(HttpStatus status, List<String> errors) {
        ControllerResponse<Void> response = ControllerResponse.<Void>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(false)
                .message(status.getReasonPhrase())
                .errors(errors)
                .build();
        return ResponseEntity.status(status.value()).body(response);
    }

    public static <T> ResponseEntity<ControllerResponse<T>> error(HttpStatus status, Map<String, String> fieldErrors) {
        ControllerResponse<T> response = ControllerResponse.<T>builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .success(false)
                .message(status.getReasonPhrase())
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(status.value()).body(response);
    }
}

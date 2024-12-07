package com.mna.springbootsecurity.base.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ControllerResponse<T> {
    private LocalDateTime timestamp;
    private boolean success;
    private T data;
    private String message;
    private List<String> errors;
    private Map<String, String> fieldErrors;
}

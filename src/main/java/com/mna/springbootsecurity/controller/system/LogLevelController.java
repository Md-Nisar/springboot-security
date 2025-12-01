package com.mna.springbootsecurity.controller.system;

import lombok.AllArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/system")
public class LogLevelController {

    private final LoggingSystem loggingSystem;

    @PostMapping("/logging/{loggerName}/{level}")
    public String changeLogLevel(@PathVariable String loggerName, @PathVariable String level) {
        try {
            loggingSystem.setLogLevel(loggerName, LogLevel.valueOf(level.toUpperCase(Locale.ROOT)));
            return "Log level for " + loggerName + " changed to " + level;
        } catch (IllegalArgumentException e) {
            return "Invalid log level. Allowed values: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF";
        }
    }
}


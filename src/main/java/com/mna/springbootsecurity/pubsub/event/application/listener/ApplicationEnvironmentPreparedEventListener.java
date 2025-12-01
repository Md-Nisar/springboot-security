package com.mna.springbootsecurity.pubsub.event.application.listener;

import com.mna.springbootsecurity.pubsub.event.application.util.PropertiesPatternMatcher;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Listener for the ApplicationEnvironmentPreparedEvent.
 * Logs various properties and memory usage based on configuration.
 */
@Slf4j
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private ConfigurableEnvironment environment;

    /**
     * Handles the ApplicationEnvironmentPreparedEvent.
     * Logs system, environment, application properties, and memory usage based on configuration.
     *
     * @param event the event to handle
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
        log.info("Application Environment Prepared Event Triggered!");
        environment = event.getEnvironment();

        boolean logEnabled = Boolean.parseBoolean(environment.getProperty("application.startup.logging.enabled"));
        if (logEnabled) {
            String logLevels = environment.getProperty("application.startup.logging.levels");
            List<String> levels = logLevels != null ? Arrays.asList(logLevels.split(",")) : List.of();

            if (levels.contains("system")) {
                logSystemProperties();
            }

            if (levels.contains("environment")) {
                logEnvironmentProperties();
            }

            if (levels.contains("application")) {
                logApplicationProperties();
            }

            if (levels.contains("memory")) {
                logMemoryUsage();
            }

        }

    }

    /**
     * Logs application properties if "application" is included in application.startup.logging.levels.
     */
    private void logApplicationProperties() {
        StringBuilder propertiesLog = new StringBuilder();

        propertiesLog.append("---- Application Properties ----");
        findPropertiesPropertySources().stream()
                .filter(ps -> PropertiesPatternMatcher.containsApplicationPropertyPattern(ps.getName()))
                .forEach(ps -> {
                    propertiesLog.append("\n# ").append(ps.getName()).append("\n");
                    propertiesLog.append(
                            Arrays.stream(ps.getPropertyNames())
                                    .sorted()
                                    .map(propName -> {
                                        String resolvedProperty = environment.getProperty(propName);
                                        String sourceProperty = Objects.requireNonNull(ps.getProperty(propName)).toString();
                                        assert resolvedProperty != null;
                                        if (resolvedProperty.equals(sourceProperty)) {
                                            return String.format(Locale.ENGLISH, "%s=%s", propName, resolvedProperty);
                                        } else {
                                            return String.format(Locale.ENGLISH, "%s=%s (OVERRIDDEN to %s)", propName, sourceProperty, resolvedProperty);
                                        }
                                    })
                                    .collect(Collectors.joining("\n"))
                    );
                });
        log.info(propertiesLog.toString());
    }

    /**
     * Logs system properties if "system" is included in application.startup.logging.levels.
     */
    private void logSystemProperties() {
        StringBuilder propertiesLog = new StringBuilder();

        propertiesLog.append("---- System Properties ----\n");
        System.getProperties().forEach((key, value) ->
                propertiesLog.append(String.format(Locale.ENGLISH, "%s=%s%n", key, value))
        );
        log.info(propertiesLog.toString());
    }

    /**
     * Logs environment variables if "environment" is included in application.startup.logging.levels.
     */
    private void logEnvironmentProperties() {
        StringBuilder propertiesLog = new StringBuilder();

        propertiesLog.append("---- Environment Variables ----\n");
        System.getenv().forEach((key, value) ->
                propertiesLog.append(String.format(Locale.ENGLISH, "%s=%s%n", key, value))
        );
        log.info(propertiesLog.toString());
    }

    /**
     * Logs memory usage if "memory" is included in application.startup.logging.levels.
     */
    private void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = (long) 1024 * 1024;
        String mega = " MB";

        String memoryLog = "--- Memory Usage ---\n" +
                String.format(Locale.ROOT, "Free memory: %s%s%n", format.format(freeMemory / mb), mega) +
                String.format(Locale.ROOT, "Allocated memory: %s%s%n", format.format(allocatedMemory / mb), mega) +
                String.format(Locale.ROOT, "Max memory: %s%s%n", format.format(maxMemory / mb), mega) +
                String.format(Locale.ROOT, "Total free memory: %s%s%n", format.format((freeMemory + (maxMemory - allocatedMemory)) / mb), mega);
        log.info(memoryLog);
    }

    /**
     * Finds property sources that are enumerable.
     *
     * @return a list of enumerable property sources
     */
    private List<EnumerablePropertySource<?>> findPropertiesPropertySources() {
        return environment.getPropertySources().stream()
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .map(ps -> (EnumerablePropertySource<?>) ps)
                .collect(Collectors.toList());
    }

    /**
     * Logs system properties, environment variables, application properties, and memory usage.
     */
    private void log() {
        logSystemProperties();
        logEnvironmentProperties();
        logApplicationProperties();
        logMemoryUsage();
    }

}

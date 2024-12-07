package com.mna.springbootsecurity.pubsub.event.application.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropertiesLogger {

    private final ConfigurableEnvironment environment;

    public void logApplicationProperties() {
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

    public void logSystemProperties() {
        StringBuilder propertiesLog = new StringBuilder();

        propertiesLog.append("---- System Properties ----\n");
        System.getProperties().forEach((key, value) ->
                propertiesLog.append(String.format(Locale.ENGLISH, "%s=%s%n", key, value))
        );
        log.info(propertiesLog.toString());
    }

    public void logEnvironmentProperties() {
        StringBuilder propertiesLog = new StringBuilder();

        propertiesLog.append("---- Environment Variables ----\n");
        System.getenv().forEach((key, value) ->
                propertiesLog.append(String.format(Locale.ENGLISH, "%s=%s%n", key, value))
        );
        log.info(propertiesLog.toString());
    }

    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = (long)1024 * 1024;
        String mega = " MB";

        String memoryLog = "--- Memory Usage ---\n" +
                String.format("Free memory: %s%s%n", format.format(freeMemory / mb), mega) +
                String.format("Allocated memory: %s%s%n", format.format(allocatedMemory / mb), mega) +
                String.format("Max memory: %s%s%n", format.format(maxMemory / mb), mega) +
                String.format("Total free memory: %s%s%n", format.format((freeMemory + (maxMemory - allocatedMemory)) / mb), mega);
        log.info(memoryLog);
    }

    public List<EnumerablePropertySource<?>> findPropertiesPropertySources() {
        return environment.getPropertySources().stream()
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .map(ps -> (EnumerablePropertySource<?>) ps)
                .collect(Collectors.toList());
    }


}

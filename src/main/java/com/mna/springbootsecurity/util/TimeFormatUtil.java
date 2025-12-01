package com.mna.springbootsecurity.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatUtil {

    private static final String DEFAULT_PATTERN = "dd MMM yyyy HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private TimeFormatUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a given timestamp (milliseconds) to a formatted string using the default pattern.
     *
     * @param timestampMillis the timestamp in milliseconds
     * @return formatted date-time string
     */
    public static String format(long timestampMillis) {
        return format(timestampMillis, DEFAULT_PATTERN);
    }

    /**
     * Converts a given timestamp (milliseconds) to a formatted string using a custom pattern.
     *
     * @param timestampMillis the timestamp in milliseconds
     * @param pattern         the desired date-time format pattern
     * @return formatted date-time string
     */
    public static String format(long timestampMillis, String pattern) {
        LocalDateTime dateTime = Instant.ofEpochMilli(timestampMillis)
                .atZone(DEFAULT_ZONE)
                .toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ROOT);
        return dateTime.format(formatter);
    }

    /**
     * Returns the current system time formatted using the default pattern.
     *
     * @return formatted current date-time string
     */
    public static String now() {
        return format(System.currentTimeMillis());
    }

    /**
     * Returns the current system time formatted using a custom pattern.
     *
     * @param pattern the desired date-time format pattern
     * @return formatted current date-time string
     */
    public static String now(String pattern) {
        return format(System.currentTimeMillis(), pattern);
    }
}


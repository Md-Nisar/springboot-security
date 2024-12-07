package com.mna.springbootsecurity.logging.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Slf4j
public class EmailLogger {

    private EmailLogger() {
    }

    private static final Marker EMAIL_MARKER = MarkerFactory.getMarker("EMAIL");

    public static void logSendEmail(String to, String subject) {
        log.info(EMAIL_MARKER, "Sending email to: {}, subject: {}", to, subject);
    }

    public static void logEmailSent() {
        log.info(EMAIL_MARKER, "Email sent successfully");
    }

    public static void logEmailServiceDisabled() {
        log.warn(EMAIL_MARKER, "Email service is disabled");
    }

    public static void logError(String actionName, Exception ex) {
        log.error(EMAIL_MARKER, "Error during {}: {}", actionName, ex.getMessage(), ex);
    }
}

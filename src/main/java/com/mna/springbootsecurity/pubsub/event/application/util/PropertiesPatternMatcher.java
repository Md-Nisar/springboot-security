package com.mna.springbootsecurity.pubsub.event.application.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PropertiesPatternMatcher {

    private PropertiesPatternMatcher() {
    }

    private static final String APPLICATION_PROPERTIES_REGEX = "application\\.properties";
    private static final String APPLICATION_PATTERN_REGEX = "application-[^\\s]*\\.properties";

    public static boolean containsApplicationPropertyPattern(String input) {
        Pattern pattern = Pattern.compile(APPLICATION_PROPERTIES_REGEX + "|" + APPLICATION_PATTERN_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}

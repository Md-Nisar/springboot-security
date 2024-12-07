package com.mna.springbootsecurity.security.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class URLUtil {

    private URLUtil() {

    }

    private static final String CHARSET = StandardCharsets.UTF_8.name();
    private static final Pattern ENCODED_PATTERN = Pattern.compile("%[0-9A-Fa-f]{2}");


    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Encoding failed for value: " + value, e);
        }
    }

    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Decoding failed for value: " + value, e);
        }
    }

    public static String[] encode(String[] values) {
        String[] encodedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            encodedValues[i] = encode(values[i]);
        }
        return encodedValues;
    }

    public static String[] decode(String[] values) {
        String[] decodedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            decodedValues[i] = decode(values[i]);
        }
        return decodedValues;
    }

    public static boolean isEncoded(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        // Check for the presence of encoded patterns
        if (ENCODED_PATTERN.matcher(url).find()) {
            // Ensure that all percent signs are followed by two hex digits
            String[] parts = url.split("%");
            for (int i = 1; i < parts.length; i++) {
                if (parts[i].length() < 2 || !isHex(parts[i].substring(0, 2))) {
                    return false; // Not properly encoded
                }
            }
            return true; // The URL is encoded
        }

        // If no percent sign is found, the URL is likely not encoded
        return false;
    }

    private static boolean isHex(String hex) {
        return hex.matches("[0-9A-Fa-f]{2}");
    }
}

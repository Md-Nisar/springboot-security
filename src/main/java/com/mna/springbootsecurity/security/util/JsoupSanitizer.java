package com.mna.springbootsecurity.security.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class JsoupSanitizer {

    private JsoupSanitizer() {
    }

    // Sanitize input for basic HTML tags
    public static String sanitizeBasicHtml(String input) {
        return Jsoup.clean(input, Safelist.basic());
    }

    // Sanitize input for simple text (no HTML tags)
    public static String sanitizePlainText(String input) {
        return Jsoup.clean(input, Safelist.none());
    }

    // Sanitize input for simple formatting (e.g., basic formatting tags)
    public static String sanitizeFormatting(String input) {
        return Jsoup.clean(input, Safelist.simpleText());
    }

    // Sanitize input for safe iframe
    public static String sanitizeIframe(String input) {
        return Jsoup.clean(input, Safelist.basicWithImages().addTags("iframe"));
    }
}


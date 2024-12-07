package com.mna.springbootsecurity.security.util;

import org.owasp.encoder.Encode;

public class OWASPEncoder {

    private OWASPEncoder() {

    }

    public static String encodeForHtml(String input) {
        return Encode.forHtml(input);
    }

    public static String encodeForJavaScript(String input) {
        return Encode.forJavaScript(input);
    }

    public static String encodeForUrl(String input) {
        return Encode.forUriComponent(input);
    }

    public static String encodeForHtmlAttribute(String input) {
        return Encode.forHtmlAttribute(input);
    }

    public static String encodeForCss(String input) {
        return Encode.forCssString(input);
    }

    public static String canonicalize(String input) {
        return ""; //ESAPI.encoder().canonicalize(input);
    }
}


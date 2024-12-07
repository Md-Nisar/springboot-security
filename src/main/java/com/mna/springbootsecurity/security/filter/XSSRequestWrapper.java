package com.mna.springbootsecurity.security.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value != null ? stripXSS(value) : null;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        String sanitizedValue = stripXSS(value);
        // Log if sanitized
        if (value != null && !value.equals(sanitizedValue)) {
            log.warn("Sanitized input from parameter: {}. Original: '{}', Sanitized: '{}'", name, value, sanitizedValue);
        }
        return sanitizedValue;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> sanitizedMap = new HashMap<>();
        Map<String, String[]> originalMap = super.getParameterMap();

        for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
            String[] values = entry.getValue();
            String[] sanitizedValues = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                sanitizedValues[i] = stripXSS(values[i]);
                if (values[i] != null && !values[i].equals(sanitizedValues[i])) {
                    log.warn("Sanitized input from parameter: {}. Original: '{}', Sanitized: '{}'", entry.getKey(), values[i], sanitizedValues[i]);
                }
            }
            sanitizedMap.put(entry.getKey(), sanitizedValues);
        }
        return sanitizedMap;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        try {
            // Read the original body, sanitize it, and return a new BufferedReader with sanitized content
            String originalBody = new String(super.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String sanitizedBody = stripXSS(originalBody);
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sanitizedBody.getBytes(StandardCharsets.UTF_8))));
        } catch (IOException e) {
            log.error("Error reading or sanitizing the request body", e);
            throw e;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String contentType = super.getContentType();
        if (contentType != null && !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return super.getInputStream(); // Bypass for non-JSON bodies
        }

        StringBuilder requestBody = getRequestBody();
        String sanitizedBody = stripXSS(requestBody.toString()); // Sanitize the JSON body


        return new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sanitizedBody.getBytes());

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException("Method not implemented!");
            }

        };
    }

    private StringBuilder getRequestBody() throws IOException {
        ServletInputStream originalInputStream = super.getInputStream();
        StringBuilder requestBody = new StringBuilder();
        byte[] buffer = new byte[1024];  // Buffer of 1KB (you can adjust the size)
        int bytesRead;

        String encoding = super.getCharacterEncoding() != null ? super.getCharacterEncoding() : StandardCharsets.UTF_8.toString();

        // Read the original stream in chunks
        while ((bytesRead = originalInputStream.read(buffer)) != -1) {
            requestBody.append(new String(buffer, 0, bytesRead, encoding));
        }
        return requestBody;
    }

    private String stripXSS(String value) {
        if (Objects.nonNull(value) && !value.trim().isEmpty()) {
            // value = OWASPEncoder.canonicalize(value);  // Canonicalize input to standardize representations
            //value = value.replaceAll("\0", "");  // Remove null characters
           // value = Jsoup.clean(value, Safelist.basic()); // Sanitize using Jsoup
        }
        return value;
    }

}

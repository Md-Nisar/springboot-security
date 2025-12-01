package com.mna.springbootsecurity.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Component
public class APIInspector {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    public String[] publicEndpoints() {
        return new String[]{
                "/api/v1/public/**",
                "/api/v1/auth/signup",
                "/api/v1/auth/verify",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh",
                "/favicon.ico",
                "/api/v1/powerbi/**"
                // Add more public endpoints as needed
        };
    }

    public String[] swaggerEndpoints() {
        return new String[]{
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/swagger-ui/**"
        };
    }

    public String[] actuatorEndpoints() {
        return new String[]{
                "/actuator",
                "/actuator/health",
                "/actuator/info",
                "/actuator/metrics",
                "/actuator/metrics/**",
                "/actuator/env",
                "/actuator/loggers",
                "/actuator/threaddump",
                "/actuator/auditevents",
                "/actuator/beans",
                "/actuator/httptrace",
                "/actuator/httpexchanges"
                // Add more actuator endpoints as needed
        };
    }

    public String[] baseEndpoints() {
        return new String[]{
                "/status"
                // Add more public endpoints as needed
        };
    }

    public boolean isPublicPath(String path) {
        for (String pattern : publicEndpoints()) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    public String extractPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }


}

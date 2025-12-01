package com.mna.springbootsecurity.security.filter;

import com.mna.springbootsecurity.security.util.APIInspector;
import com.mna.springbootsecurity.security.util.SecurityContextUtil;
import com.mna.springbootsecurity.util.ExecutionContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecutionContextFilter extends OncePerRequestFilter {

    private final APIInspector apiUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("Processing ExecutionContextFilter - {} {}", request.getMethod(), request.getRequestURI());

        // Skip filter processing for public paths
        String path = apiUtil.extractPath(request);
        if (apiUtil.isPublicPath(path)) {
            log.debug("Skipping ExecutionContext processing for public API: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract relevant information from the Request
            String requestId = request.getHeader("X-Request-ID");
            if (requestId == null || requestId.isBlank()) {
                requestId = "REQ-" + UUID.randomUUID();
            }

            String authToken = request.getHeader("Authorization");
            String clientIp = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            long startTime = System.currentTimeMillis();

            // Store values in ExecutionContext
            ExecutionContextHolder.put(ExecutionContextHolder.REQUEST_ID, requestId);
            ExecutionContextHolder.put(ExecutionContextHolder.AUTH_TOKEN, authToken);
            ExecutionContextHolder.put(ExecutionContextHolder.CLIENT_IP, clientIp);
            ExecutionContextHolder.put(ExecutionContextHolder.USER_AGENT, userAgent);
            ExecutionContextHolder.put(ExecutionContextHolder.START_TIME, startTime);

            if (SecurityContextUtil.isSecurityContextAvailable()) {
                ExecutionContextHolder.put(ExecutionContextHolder.USER, SecurityContextUtil.getUser());
            }

            // Set MDC for logging correlation
            MDC.put("requestId", requestId);

            filterChain.doFilter(request, response);
        } finally {
            // Clean up thread-local storage to prevent memory leaks
            ExecutionContextHolder.clear();
            MDC.clear();
            log.debug("Cleared ExecutionContext! - {} {}", request.getMethod(), request.getRequestURI());
        }
    }
}

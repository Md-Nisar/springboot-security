package com.mna.springbootsecurity.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class XSSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing XSSFilter...");

        // Wrap the request to sanitize the parameters
        HttpServletRequest sanitizedRequest = new XSSRequestWrapper(request);

        // Continue the filter chain with sanitized request
        filterChain.doFilter(sanitizedRequest, response);
    }

}

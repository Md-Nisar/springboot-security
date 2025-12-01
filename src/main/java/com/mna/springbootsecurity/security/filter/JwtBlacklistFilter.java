package com.mna.springbootsecurity.security.filter;

import com.mna.springbootsecurity.cache.service.JwtTokenCacheService;
import com.mna.springbootsecurity.security.exception.JwtTokenBlacklistedException;
import com.mna.springbootsecurity.security.util.APIInspector;
import com.mna.springbootsecurity.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final JwtTokenCacheService jwtTokenCacheService;
    private final JwtUtil jwtUtil;
    private final APIInspector apiUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {

            // Skip filter processing for public paths
            String path = apiUtil.extractPath(request);
            if (apiUtil.isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Skip filter processing if token not found in the request
            String token = jwtUtil.extractTokenFromRequest(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            log.debug("Processing JwtBlacklistFilter - {} {}", request.getMethod(), request.getRequestURI());
            // Check if token is blacklisted
            String username = jwtUtil.extractSubject(token);
            if (jwtTokenCacheService.isTokenBlacklisted(username, token)) {
                throw new JwtTokenBlacklistedException("JWT token is blacklisted");
            }

            filterChain.doFilter(request, response);

        } catch (ServletException | IOException e) {
            log.error("Error processing request: {}", e.getMessage());
            throw new ServletException("Error processing request", e);
        }
    }


}


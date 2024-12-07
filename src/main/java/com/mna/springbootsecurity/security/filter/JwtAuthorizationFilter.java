package com.mna.springbootsecurity.security.filter;

import com.mna.springbootsecurity.security.exception.JwtTokenBlacklistedException;
import com.mna.springbootsecurity.security.exception.JwtTokenExpiredException;
import com.mna.springbootsecurity.security.exception.JwtTokenInvalidException;
import com.mna.springbootsecurity.security.util.APIInspector;
import com.mna.springbootsecurity.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final APIInspector apiUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Skip filter processing for public paths
        String path = apiUtil.extractPath(request);
        if (apiUtil.isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip filter processing if access token not found in the request
        String accessToken = jwtUtil.extractTokenFromRequest(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Processing JwtAuthorizationFilter...");

        try {
            String username = jwtUtil.extractSubject(accessToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Check if access token is expired
                if (jwtUtil.isTokenExpired(accessToken)) {
                    throw new JwtTokenExpiredException("JWT access token has expired");
                }

                // Validate the access token
                if (!jwtUtil.validateToken(accessToken, userDetails.getUsername())) {
                    throw new JwtTokenInvalidException("JWT access token is invalid");
                }

                // Set authentication in SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (JwtTokenExpiredException | JwtTokenInvalidException | JwtTokenBlacklistedException e) {
            log.error("JWT error: {}", e.getMessage());
            SecurityContextHolder.clearContext(); // Clear security context in case of errors related to JWT
        } catch (Exception e) {
            log.error("Error authenticating user", e);
            SecurityContextHolder.clearContext(); // Clear security context for other exceptions
        }

        filterChain.doFilter(request, response);
    }
}

package com.mna.springbootsecurity.security.config;

import com.mna.springbootsecurity.base.enums.Roles;
import com.mna.springbootsecurity.base.property.ApplicationProperties;
import com.mna.springbootsecurity.security.filter.*;
import com.mna.springbootsecurity.security.handler.CustomAccessDeniedHandler;
import com.mna.springbootsecurity.security.handler.CustomAuthenticationEntryPoint;
import com.mna.springbootsecurity.security.util.APIInspector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtBlacklistFilter jwtBlacklistFilter;
    private final XSSFilter xssFilter;
    private final ExecutionContextFilter executionContextFilter;
    private final DataSourceContextFilter dataSourceContextFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final APIInspector apiInspector;
    private final ApplicationProperties applicationProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring 'SecurityFilterChain'...");
        http
                .csrf(AbstractHttpConfigurer::disable) // For stateless APIs (like JWT-based authentication)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API doesn't use server-side sessions (JWT-based stateless authentication)
                .headers(headers -> {
                    headers
                            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; object-src 'none'; style-src 'self';"))
                            .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Prevent ClickJacking
                            .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable)  // Prevent MIME-sniffing
                            .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER));
                    if (!applicationProperties.isLocalEnvironment()) {
                        headers
                                .httpStrictTransportSecurity(hsts -> hsts // Enforces HTTPS
                                        .includeSubDomains(true)
                                        .preload(true)
                                        .maxAgeInSeconds(31536000)
                                );
                    }
                }).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(apiInspector.publicEndpoints()).permitAll()
                        .requestMatchers(apiInspector.baseEndpoints()).permitAll()
                        .requestMatchers(apiInspector.actuatorEndpoints()).permitAll()
                        .requestMatchers(apiInspector.swaggerEndpoints()).permitAll()
                        .requestMatchers("/api/v1/user/**").hasAnyRole(Roles.USER.name(), Roles.ADMIN.name(), Roles.SYSTEM.name())
                        .requestMatchers("/api/v1/admin/**").hasAnyRole(Roles.ADMIN.name(), Roles.SYSTEM.name())
                        .requestMatchers("/api/v1/system/**").hasRole(Roles.SYSTEM.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtBlacklistFilter, JwtAuthorizationFilter.class)
                .addFilterBefore(executionContextFilter, JwtBlacklistFilter.class)
                .addFilterBefore(dataSourceContextFilter, ExecutionContextFilter.class)
                .addFilterBefore(xssFilter, DataSourceContextFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );


        log.info("'SecurityFilterChain' configuration completed!");
        return http.build();
    }

}

package com.mna.springbootsecurity.security.filter;


import com.mna.springbootsecurity.datasource.context.DataSourceContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class DataSourceContextFilter extends OncePerRequestFilter {

    private static final String DATASOURCE_HEADER = "X-Data-Source";

    @Value("${application.multi-datasource.enabled:false}")
    private boolean isMultiDataSourceEnabled;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Proceed with the filter only if multi-datasource is enabled
        if (isMultiDataSourceEnabled) {
            String dataSourceKey = request.getHeader(DATASOURCE_HEADER);
            if (dataSourceKey != null && !dataSourceKey.isEmpty()) {
                log.info("Received DataSource header: {}", dataSourceKey);
                DataSourceContextHolder.set(dataSourceKey);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (isMultiDataSourceEnabled) {
                log.info("Initiating Clearing of DataSource context after request");
                DataSourceContextHolder.clear();
            }
        }
    }
}


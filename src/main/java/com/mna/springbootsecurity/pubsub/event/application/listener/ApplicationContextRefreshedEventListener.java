package com.mna.springbootsecurity.pubsub.event.application.listener;

import jakarta.servlet.Filter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private final FilterChainProxy filterChainProxy;
    private final ListableBeanFactory beanFactory;

    @Value("${application.event.logging.enabled:false}")
    private boolean loggingEnabled;

    @Value("${application.event.logging.categories}")
    private String loggingCategories;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        log.info(">>> Application Context Refresh Event Triggered!");

        if (loggingEnabled) {
            Set<String> categories = loggingCategories != null
                    ? new HashSet<>(Arrays.asList(loggingCategories.split(",")))
                    : Set.of();

            if (categories.contains("filters")) {
                logSecurityFilters();
            }

            if (categories.contains("beans")) {
                logBeans();
            }

        }

    }

    private void logBeans() {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        List<String> sortedBeanNames = Arrays.stream(beanNames)
                .map(String::trim)
                .sorted()
                .toList();

        log.debug("---- Logging All Registered Beans (Total: {}) ----", sortedBeanNames.size());

        int index = 1;
        for (String beanName : sortedBeanNames) {
            log.debug("{} - {}", index, beanName);
            index++;
        }
    }

    private void logSecurityFilters() {
        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
        log.debug("--- Logging All Security Filters (Total Chains: {})  ----", filterChains.size());

        int chainIndex = 1;
        for (SecurityFilterChain chain : filterChains) {
            log.debug("Security Filter Chain #{}:", chainIndex);
            List<Filter> filters = chain.getFilters();
            int filterOrder = 1;
            for (Filter filter : filters) {
                log.debug("{} - {}", filterOrder, filter.getClass().getSimpleName());
                filterOrder++;
            }
            chainIndex++;
        }
    }
}

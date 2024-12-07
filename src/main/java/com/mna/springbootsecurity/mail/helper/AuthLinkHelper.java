package com.mna.springbootsecurity.mail.helper;

import com.mna.springbootsecurity.base.property.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthLinkHelper {

    private final ApplicationProperties appProperties;

    public String generateUserVerificationLink(String username, String token, boolean useClientUrl) {
        return generateLink(useClientUrl, "/auth/verify", Map.of(
                "username", username,
                "token", token
        ));
    }

    public String generateResetPasswordLink(String username, String token, boolean useClientUrl) {
        return generateLink(useClientUrl, "/auth/resetPassword", Map.of(
                "username", username,
                "token", token
        ));
    }

    // Flexible method to handle different types of links with varying query parameters
    public String generateLink(boolean useClientUrl, String endpoint, Map<String, String> queryParams) {
        String baseUrl = getBaseUrl(useClientUrl);
        String queryString = buildQueryString(queryParams);
        return baseUrl + endpoint + queryString;
    }

    // Common method to get the base URL
    private String getBaseUrl(boolean useClientUrl) {
        return useClientUrl ? appProperties.getClient().getBaseUrl() : appProperties.getServer().getBaseUrl();
    }

    // Build the query string dynamically from the map of parameters
    private String buildQueryString(Map<String, String> queryParams) {
        if (queryParams.isEmpty()) {
            return "";
        }
        return "?" + queryParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}

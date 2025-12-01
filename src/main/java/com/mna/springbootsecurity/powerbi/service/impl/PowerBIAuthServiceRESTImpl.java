package com.mna.springbootsecurity.powerbi.service.impl;

import com.mna.springbootsecurity.powerbi.config.PowerBIConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.mna.springbootsecurity.powerbi.base.constants.PowerBIURL.AUTHORITY_URL;
import static com.mna.springbootsecurity.powerbi.base.constants.PowerBIURL.BASE_SCOPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PowerBIAuthServiceRESTImpl {

    private final PowerBIConfig powerBIConfig;
    private final RestTemplate restTemplate = new RestTemplate();


    /**
     * Determines the authentication type and calls the respective method
     */
    public String getAccessToken() {
        log.info("Fetching Power BI access token...");

        if ("ServicePrincipal".equalsIgnoreCase(powerBIConfig.getAuthenticationType())) {
            return getAccessTokenWithServicePrincipal();
        } else if ("MasterUser".equalsIgnoreCase(powerBIConfig.getAuthenticationType())) {
            return getAccessTokenWithMasterUser();
        } else {
            throw new IllegalStateException("Invalid authentication type: " + powerBIConfig.getAuthenticationType());
        }
    }

    /**
     * Authentication using Service Principal (Client Credentials)
     */
    private String getAccessTokenWithServicePrincipal() {
        log.info("Authenticating with Service Principal...");
        String authorityUrl = AUTHORITY_URL.replace("{tenantId}", powerBIConfig.getTenantId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("scope", BASE_SCOPE);
        bodyParams.put("grant_type", "client_credentials");
        bodyParams.put("client_id", powerBIConfig.getClientId());
        bodyParams.put("client_secret", powerBIConfig.getClientSecret());

        return fetchAccessToken(authorityUrl, bodyParams, headers);
    }

    /**
     * Authentication using Master User credentials
     */
    private String getAccessTokenWithMasterUser() {
        log.info("Authenticating with Master User...");
        String authorityUrl = AUTHORITY_URL.replace("{tenantId}", "common");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("scope", BASE_SCOPE);
        bodyParams.put("grant_type", "password");
        bodyParams.put("client_id", powerBIConfig.getClientId());
        bodyParams.put("username", powerBIConfig.getPbiUsername());
        bodyParams.put("password", powerBIConfig.getPbiPassword());

        return fetchAccessToken(authorityUrl, bodyParams, headers);
    }

    /**
     * Helper method to send HTTP request and return access token
     */
    private String fetchAccessToken(String url, Map<String, String> bodyParams, HttpHeaders headers) {
        HttpEntity<Map<String, String>> request = new HttpEntity<>(bodyParams, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String accessToken = (String) response.getBody().get("access_token");
            log.info("Power BI Access Token retrieved successfully.");
            return accessToken;
        } else {
            log.error("Failed to retrieve Power BI Access Token. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to retrieve Power BI Access Token");
        }
    }
}


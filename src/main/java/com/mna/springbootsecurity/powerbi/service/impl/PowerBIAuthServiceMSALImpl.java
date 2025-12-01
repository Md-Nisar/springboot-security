package com.mna.springbootsecurity.powerbi.service.impl;

import com.microsoft.aad.msal4j.*;
import com.mna.springbootsecurity.powerbi.base.enums.AuthenticationType;
import com.mna.springbootsecurity.powerbi.config.PowerBIConfig;
import com.mna.springbootsecurity.powerbi.service.PowerBIAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.mna.springbootsecurity.powerbi.base.constants.PowerBIURL.AUTHORITY_URL;
import static com.mna.springbootsecurity.powerbi.base.constants.PowerBIURL.BASE_SCOPE;


@Slf4j
@Service
@RequiredArgsConstructor
public class PowerBIAuthServiceMSALImpl implements PowerBIAuthService {

    private final PowerBIConfig config;

    @Override
    public String getAccessToken() {
        try {
            if (config.getAuthenticationType().equalsIgnoreCase(AuthenticationType.MASTER_USER.getValue())) {
                return getAccessTokenUsingMasterUser();
            } else if (config.getAuthenticationType().equalsIgnoreCase(AuthenticationType.SERVICE_PRINCIPAL.getValue())) {
                return getAccessTokenUsingServicePrincipal();
            } else {
                throw new IllegalArgumentException("Invalid authentication type: " + config.getAuthenticationType());
            }
        } catch (Exception e) {
            log.error("Error fetching access token: ", e);
            return null;
        }
    }

    private String getAccessTokenUsingServicePrincipal() throws Exception {
        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                        config.getClientId(),
                        ClientCredentialFactory.createFromSecret(config.getClientSecret()))
                .authority(AUTHORITY_URL.replace("{tenantId}", config.getTenantId()))
                .build();

        ClientCredentialParameters parameters = ClientCredentialParameters.builder(
                        Collections.singleton(BASE_SCOPE))
                .build();

        IAuthenticationResult result = app.acquireToken(parameters).get();
        return result != null ? result.accessToken() : null;
    }

    private String getAccessTokenUsingMasterUser() throws Exception {
        PublicClientApplication app = PublicClientApplication.builder(config.getClientId())
                .authority(AUTHORITY_URL.replace("{tenantId}", "organizations"))
                .build();

        UserNamePasswordParameters parameters = UserNamePasswordParameters.builder(
                        Collections.singleton(BASE_SCOPE),
                        config.getPbiUsername(),
                        config.getPbiPassword().toCharArray())
                .build();

        IAuthenticationResult result = app.acquireToken(parameters).get();
        return result != null ? result.accessToken() : null;
    }
}
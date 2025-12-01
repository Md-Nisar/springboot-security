package com.mna.springbootsecurity.powerbi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "powerbi")
public class PowerBIConfig {

    /**
     * Application ID (Client ID) from Azure AD
     */
    private String clientId;

    /**
     * Client Secret (App Secret) from Azure AD
     */
    private String clientSecret;

    /**
     * Tenant ID from Azure AD
     */
    private String tenantId;

    /**
     * Authentication Type: MasterUser or ServicePrincipal
     */
    private String authenticationType;

    /**
     * Authority URL for OAuth2 authentication
     */
    private String authorityUrl;

    /**
     * Power BI API Scope
     */
    private String scopeBase;

    /**
     * Master User Credentials (Only needed if using MasterUser authentication)
     */
    private String pbiUsername;
    private String pbiPassword;
}

package com.mna.springbootsecurity.powerbi.base.enums;

import lombok.Getter;

@Getter
public enum AuthenticationType {

    SERVICE_PRINCIPAL("SERVICE_PRINCIPAL"),
    MASTER_USER("MASTER_USER");

    private final String value;

    AuthenticationType(String value) {
        this.value = value;
    }

}


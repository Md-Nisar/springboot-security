package com.mna.springbootsecurity.powerbi.base.enums;

import lombok.Getter;

@Getter
public enum GrantType {

    CLIENT_CREDENTIALS("client_credentials"),
    PASSWORD("password");

    private final String value;

    GrantType(String value) {
        this.value = value;
    }
}

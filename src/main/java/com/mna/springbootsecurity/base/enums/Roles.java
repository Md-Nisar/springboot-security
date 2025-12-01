package com.mna.springbootsecurity.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    SYSTEM("ROLE_SYSTEM");

    private final String value;
}

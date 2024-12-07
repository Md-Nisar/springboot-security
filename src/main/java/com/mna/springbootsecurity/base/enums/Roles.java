package com.mna.springbootsecurity.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}

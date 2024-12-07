package com.mna.springbootsecurity.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permissions {

    USER(new String[]{"/api/v1/user/**"}),
    ADMIN(new String[]{"/api/v1/user/**", "/api/v1/admin/**"});

    private final String[] patterns;
}

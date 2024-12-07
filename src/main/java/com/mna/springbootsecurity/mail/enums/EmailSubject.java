package com.mna.springbootsecurity.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailSubject {

    WELCOME("Welcome!"),
    USER_VERIFICATION_LINK("User Verification Link"),
    RESET_PASSWORD_LINK("Reset Password Link");

    private final String value;


}

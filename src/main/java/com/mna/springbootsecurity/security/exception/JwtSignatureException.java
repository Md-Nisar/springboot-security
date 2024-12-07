package com.mna.springbootsecurity.security.exception;

public class JwtSignatureException extends RuntimeException {

    public JwtSignatureException(String message) {
        super(message);
    }
}

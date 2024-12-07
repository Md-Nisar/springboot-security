package com.mna.springbootsecurity.security.exception;

public class JwtTokenBlacklistedException extends RuntimeException {
    public JwtTokenBlacklistedException(String message) {
        super(message);
    }
}

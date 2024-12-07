package com.mna.springbootsecurity.cache.service;

public interface VerificationTokenCacheService {

    void storeToken(String key, String token);

    boolean verifyToken(String key, String token);

    void deleteToken(String key);


}

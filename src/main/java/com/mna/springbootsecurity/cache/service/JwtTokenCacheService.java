package com.mna.springbootsecurity.cache.service;

import com.mna.springbootsecurity.base.vo.JwtTokenData;

public interface JwtTokenCacheService {

    void storeToken(String key, JwtTokenData jwTokenData);

    void storeTokenWithExpiry(String key, JwtTokenData jwTokenData);

    JwtTokenData retrieveToken(String jwTokenData);

    void deleteToken(String key);

    void blacklistToken(String keySuffix, String token);

    boolean isTokenBlacklisted(String keySuffix, String token);

    void clearAllBlacklistedTokens();

    void clearExpiredBlacklistedTokens();
}

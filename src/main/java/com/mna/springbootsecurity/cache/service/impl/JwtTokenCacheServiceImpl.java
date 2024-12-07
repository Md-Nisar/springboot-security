package com.mna.springbootsecurity.cache.service.impl;

import com.mna.springbootsecurity.cache.constant.CacheKey;
import com.mna.springbootsecurity.base.vo.JwtTokenData;
import com.mna.springbootsecurity.cache.service.CacheService;
import com.mna.springbootsecurity.cache.service.JwtTokenCacheService;
import com.mna.springbootsecurity.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenCacheServiceImpl implements JwtTokenCacheService {

    private final CacheService cacheService;

    private final JwtUtil jwtUtil;

    @Override
    public void storeToken(String key, JwtTokenData jwTokenData) {
        cacheService.setValue(CacheKey.JWT_ACCESS_TOKEN + key, jwTokenData);
        log.info("Token data stored successfully for key '{}'.", key);
    }

    @Override
    public void storeTokenWithExpiry(String key, JwtTokenData jwTokenData) {
        long expiryInSeconds = Duration.between(LocalDateTime.now(), LocalDateTime.ofInstant(jwTokenData.getExpiration().toInstant(), ZoneId.systemDefault())).getSeconds();
        cacheService.setValue(CacheKey.JWT_ACCESS_TOKEN + key, jwTokenData, expiryInSeconds, TimeUnit.SECONDS);
        log.info("Token data stored successfully for key '{}'.", CacheKey.JWT_ACCESS_TOKEN + key);
    }

    @Override
    public JwtTokenData retrieveToken(String key) {
        return (JwtTokenData) cacheService.getValue(CacheKey.JWT_ACCESS_TOKEN + key);
    }

    @Override
    public void deleteToken(String key) {
        cacheService.deleteKey(CacheKey.JWT_ACCESS_TOKEN + key);
        log.info("Token data evicted successfully for key '{}'.", CacheKey.JWT_ACCESS_TOKEN + key);
    }

    @Override
    public void blacklistToken(String keySuffix, String token) {
        cacheService.addToSet(CacheKey.JWT_BLACKLIST + keySuffix, token);
    }

    @Override
    public boolean isTokenBlacklisted(String keySuffix, String token) {
        return cacheService.isMemberOfSet(CacheKey.JWT_BLACKLIST + keySuffix, token);
    }

    @Override
    public void clearAllBlacklistedTokens() {
        cacheService.deleteKey(CacheKey.JWT_BLACKLIST + "*");
        log.info("Cleared all blacklisted tokens.");
    }

    @Override
    public void clearExpiredBlacklistedTokens() {
        Set<String> keys = cacheService.getKeys(CacheKey.JWT_BLACKLIST + "*");

        if (keys == null || keys.isEmpty()) {
            log.info("No keys found for blacklisted tokens.");
            return;
        }

        for (String key : keys) {
            Set<Object> tokens = cacheService.getValues(key);
            if (tokens == null || tokens.isEmpty()) continue;

            tokens.stream()
                    .map(Object::toString)
                    .filter(jwtUtil::isTokenExpired)
                    .forEach(token -> {
                        cacheService.removeFromSet(key, token);
                        log.info("Expired token evicted from the blacklist cache successfully! Username: {}, Token: {}", key, token);
                    });
        }
    }

}

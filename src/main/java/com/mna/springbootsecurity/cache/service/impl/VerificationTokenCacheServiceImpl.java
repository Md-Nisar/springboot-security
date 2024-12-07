package com.mna.springbootsecurity.cache.service.impl;

import com.mna.springbootsecurity.cache.constant.CacheKey;
import com.mna.springbootsecurity.cache.service.CacheService;
import com.mna.springbootsecurity.cache.service.VerificationTokenCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenCacheServiceImpl implements VerificationTokenCacheService {

    private final CacheService cacheService;

    @Override
    public void storeToken(String key, String token) {
        cacheService.setValue(CacheKey.SIGNUP_VERIFICATION_TOKEN + key, token, 1, TimeUnit.HOURS);
        log.info("Verification token stored successfully with key '{}'.", CacheKey.SIGNUP_VERIFICATION_TOKEN + key);
    }

    public boolean verifyToken(String key, String token) {
        Object cachedTokenObject = cacheService.getValue(CacheKey.SIGNUP_VERIFICATION_TOKEN + key);

        if (cachedTokenObject == null) {
            return false;
        }

        String cachedToken = cachedTokenObject.toString();
        return cachedToken.equals(token);

    }

    @Override
    public void deleteToken(String key) {
        cacheService.deleteKey(CacheKey.SIGNUP_VERIFICATION_TOKEN + key);
        log.info("Verification token evicted successfully for key '{}'.", CacheKey.SIGNUP_VERIFICATION_TOKEN + key);
    }
}

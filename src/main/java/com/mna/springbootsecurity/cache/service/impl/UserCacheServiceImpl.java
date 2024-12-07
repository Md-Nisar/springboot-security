package com.mna.springbootsecurity.cache.service.impl;

import com.mna.springbootsecurity.cache.constant.CacheKey;
import com.mna.springbootsecurity.cache.service.CacheService;
import com.mna.springbootsecurity.cache.service.UserCacheService;
import com.mna.springbootsecurity.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCacheServiceImpl implements UserCacheService {

    private final CacheService cacheService;

    @Override
    public void storeUser(String key, User user) {
        cacheService.setValue(CacheKey.SIGNUP_REGISTRATION_USER + key, user, 1, TimeUnit.HOURS);
        log.info("User Registration data stored successfully with key '{}'.", CacheKey.SIGNUP_REGISTRATION_USER + key);
    }

    @Override
    public User retrieveUser(String key) {
        return (User) cacheService.getValue(CacheKey.SIGNUP_REGISTRATION_USER + key);
    }

    @Override
    public void deleteUser(String key) {
        cacheService.deleteKey(CacheKey.SIGNUP_REGISTRATION_USER + key);
        log.info("User data evicted successfully for key '{}'.", CacheKey.SIGNUP_VERIFICATION_TOKEN + key);

    }
}

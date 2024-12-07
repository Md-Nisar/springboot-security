package com.mna.springbootsecurity.cache.service.impl;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Profile(Profiles.REDIS)
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValue(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Set<Object> getValues(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeFromSet(String key, Object value) {
        BoundSetOperations<String, Object> setOperations = redisTemplate.boundSetOps(key);
        setOperations.remove(value);
    }

    @Override
    public void addToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public boolean isMemberOfSet(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }
}

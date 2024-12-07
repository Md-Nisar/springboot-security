package com.mna.springbootsecurity.cache.config;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.cache.service.CacheService;
import com.mna.springbootsecurity.cache.service.impl.RedisCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Slf4j
public class CacheServiceConfiguration {

    @Profile(Profiles.REDIS)
    @Bean
    public CacheService redisCacheService(RedisTemplate<String, Object> redisTemplate) {
        log.info("Using Redis as the CacheService...");
        return new RedisCacheServiceImpl(redisTemplate);
    }


}

package com.mna.springbootsecurity.cache.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheService {

    void setValue(String key, Object value);

    void setValue(String key, Object value, long timeout, TimeUnit unit);

    Object getValue(String key);

    Set<String> getKeys(String pattern);

    void deleteKey(String key);

    Set<Object> getValues(String key);

    void removeFromSet(String key, Object value);

    void addToSet(String key, Object value);

    boolean isMemberOfSet(String key, Object value);


}

package com.mna.springbootsecurity.cache.service;

import com.mna.springbootsecurity.domain.entity.User;

public interface UserCacheService {

    void storeUser(String key, User user);

    User retrieveUser(String key);

    void deleteUser(String key);


}

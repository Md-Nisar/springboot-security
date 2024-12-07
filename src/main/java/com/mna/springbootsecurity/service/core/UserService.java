package com.mna.springbootsecurity.service.core;

import com.mna.springbootsecurity.base.vo.UserData;

public interface UserService {

    UserData getUserByUsername(String username);

    UserData updateUser(String name, UserData userData);

    boolean deleteUserByUsername(String username);

    boolean existsByUsername(String username);

}

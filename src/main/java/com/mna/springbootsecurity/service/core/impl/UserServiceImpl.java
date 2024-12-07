package com.mna.springbootsecurity.service.core.impl;

import com.mna.springbootsecurity.base.vo.RoleData;
import com.mna.springbootsecurity.base.vo.UserData;
import com.mna.springbootsecurity.domain.dao.UserDao;
import com.mna.springbootsecurity.domain.entity.User;
import com.mna.springbootsecurity.service.core.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Transactional(readOnly = true)
    public UserData getUserByUsername(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<RoleData> roles = user.getRoles().stream().map(role -> RoleData.builder()
                .id(role.getId())
                .name(role.getName())
                .build()
        ).collect(Collectors.toSet());

        return UserData.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional
    public UserData updateUser(String username, UserData userData) {
        UserData existingUserData = getUserByUsername(username);
        User user = new User();
        user.setId(existingUserData.getId());
        user.setEmail(userData.getEmail());
        User savedUser = userDao.save(user);
        return UserData.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

    @Transactional
    public boolean deleteUserByUsername(String username) {
        try {
            Optional<User> userOptional = userDao.findByUsername(username);
            userOptional.ifPresent(userDao::delete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        try {
             return userDao.existsByUsername(username);
        } catch (Exception e) {
            return false;
        }
    }
}

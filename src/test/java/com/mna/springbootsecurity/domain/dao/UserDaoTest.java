package com.mna.springbootsecurity.domain.dao;

import com.mna.springbootsecurity.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("Steve Rogers");
        user.setEmail("steve.rogers@example.com");
        user.setPassword("Steve@123");
        user.setEnabled(true);
        user.setRoles(Collections.emptySet());
        userDao.save(user);
    }

    @Test
    void findByUsername() {
        Optional<User> foundUser = userDao.findByUsername("Steve Rogers");
        assertTrue(foundUser.isPresent());
        assertNotNull(foundUser.get().getId());
        assertEquals("Steve Rogers", foundUser.get().getUsername());
    }

    @Test
    void findByEmail() {
        Optional<User> foundUser = userDao.findByEmail("steve.rogers@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("steve.rogers@example.com", foundUser.get().getEmail());
    }

    @Test
    void existsByUsername() {
        boolean exists = userDao.existsByUsername("Steve Rogers");
        assertTrue(exists);
    }

    @Test
    void existsByEmail() {
        boolean exists = userDao.existsByEmail("steve.rogers@example.com");
        assertTrue(exists);
    }

    @AfterEach
    void tearDown() {
        userDao.deleteAll();
    }
}
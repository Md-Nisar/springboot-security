package com.mna.springbootsecurity.service.core;

import com.mna.springbootsecurity.base.vo.UserData;
import com.mna.springbootsecurity.domain.dao.UserDao;
import com.mna.springbootsecurity.domain.entity.User;
import com.mna.springbootsecurity.service.core.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setId(1L);
        user.setUsername("Steve Rogers");
        user.setEmail("steve.rogers@example.com");
        user.setPassword("Steve@123");
        user.setEnabled(true);
        user.setRoles(Collections.emptySet());
        when(userDao.findByUsername("Steve Rogers")).thenReturn(Optional.of(user));
        when(userDao.findByEmail("steve.rogers@example.com")).thenReturn(Optional.of(user));
        when(userDao.existsByUsername("Steve Rogers")).thenReturn(true);
        when(userDao.existsByEmail("steve.rogers@example.com")).thenReturn(true);
        when(userDao.save(user)).thenReturn(user);
    }

    @Test
    void getUserByUsername() {
        UserData userData = userService.getUserByUsername("Steve Rogers");
        assertNotNull(userData);
        assertEquals("Steve Rogers", userData.getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userDao.findByUsername("Tony Stark")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("Tony Stark"));
    }

    @Test
    void updateUser() {
        UserData userData = UserData.builder().email("new.email@example.com").build();
        UserData updatedUser = userService.updateUser("Steve Rogers", userData);
        assertNotNull(updatedUser);
        assertEquals("new.email@example.com", updatedUser.getEmail());
    }

    @Test
    void deleteUserByUsername() {
        boolean deleted = userService.deleteUserByUsername("Steve Rogers");
        assertTrue(deleted);
        verify(userDao, times(1)).delete(any(User.class));
    }

    @Test
    void existsByUsername() {
        boolean exists = userService.existsByUsername("Steve Rogers");
        assertTrue(exists);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
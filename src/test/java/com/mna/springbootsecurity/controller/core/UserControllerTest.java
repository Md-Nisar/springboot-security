package com.mna.springbootsecurity.controller.core;

import com.mna.springbootsecurity.base.vo.UserData;
import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("Steve Rogers");
    }

    @Test
    void getUserDetails() {
        UserData userData = UserData.builder().username("Steve Rogers").build();
        when(userService.getUserByUsername("Steve Rogers")).thenReturn(userData);

        ResponseEntity<ControllerResponse<UserData>> response = userController.getUserDetails(userDetails);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Steve Rogers", response.getBody().getData().getUsername());
    }

    @Test
    void updateUser() {
        UserData userData = UserData.builder().email("new.email@example.com").build();
        UserData updatedUserData = UserData.builder().username("Steve Rogers").email("new.email@example.com").build();
        when(userService.updateUser("Steve Rogers", userData)).thenReturn(updatedUserData);

        ResponseEntity<ControllerResponse<UserData>> response = userController.updateUser(userDetails, userData);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new.email@example.com", response.getBody().getData().getEmail());
    }

    @Test
    void deleteUser() {
        when(userService.deleteUserByUsername("Steve Rogers")).thenReturn(true);

        ResponseEntity<ControllerResponse<Boolean>> response = userController.deleteUser(userDetails);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
    }
}

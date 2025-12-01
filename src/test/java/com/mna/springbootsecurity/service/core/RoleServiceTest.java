package com.mna.springbootsecurity.service.core;

import com.mna.springbootsecurity.domain.dao.RoleDao;
import com.mna.springbootsecurity.domain.entity.Role;
import com.mna.springbootsecurity.service.core.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;


    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        roleUser = new Role();
        roleUser.setName("ROLE_USER");

        roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
    }

    @Test
    public void testFindByName() {
        when(roleDao.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));

        Role foundRole = roleService.findByName("ROLE_USER");
        assertThat(foundRole.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    public void testExistsByName() {
        when(roleDao.existsByName("ROLE_ADMIN")).thenReturn(true);

        boolean exists = roleService.existsByName("ROLE_ADMIN");
        assertThat(exists).isTrue();
    }
}

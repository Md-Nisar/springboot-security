package com.mna.springbootsecurity.domain.dao;

import com.mna.springbootsecurity.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RoleDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName("ROLE_USER");
        entityManager.persistAndFlush(role);

        Optional<Role> foundRole = roleDao.findByName("ROLE_USER");
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    public void testExistsByName() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        entityManager.persistAndFlush(role);

        boolean exists = roleDao.existsByName("ROLE_ADMIN");
        assertTrue(exists);
    }
}

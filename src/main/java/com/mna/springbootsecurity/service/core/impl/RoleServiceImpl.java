package com.mna.springbootsecurity.service.core.impl;

import com.mna.springbootsecurity.domain.dao.RoleDao;
import com.mna.springbootsecurity.domain.entity.Role;
import com.mna.springbootsecurity.service.core.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        Optional<Role> role = roleDao.findByName(name);
        return role.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(String name) {
        return  roleDao.existsByName(name);
    }
}

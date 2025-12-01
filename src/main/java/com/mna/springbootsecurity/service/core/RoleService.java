package com.mna.springbootsecurity.service.core;

import com.mna.springbootsecurity.domain.entity.Role;

public interface RoleService {

    Role findByName(String name);

    Boolean existsByName(String name);

}

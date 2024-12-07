package com.mna.springbootsecurity.base.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private Long id;
    private String username;
    private String email;
    private Set<RoleData> roles;
}

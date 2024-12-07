package com.mna.springbootsecurity.base.vo.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String username;
    private String email;
    private String password;
}

package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenData {

    private String refreshToken;
    private String accessToken;
}


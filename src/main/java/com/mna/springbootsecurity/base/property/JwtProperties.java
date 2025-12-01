package com.mna.springbootsecurity.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
public class JwtProperties {

    private String secret;
    private String tokenPrefix;
    private Long accessTokenExpirationTime;
    private Long refreshTokenExpirationTime;
}

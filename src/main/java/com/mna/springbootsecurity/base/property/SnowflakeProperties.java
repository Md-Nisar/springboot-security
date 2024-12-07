package com.mna.springbootsecurity.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.snowflake")
@Getter
@Setter
public class SnowflakeProperties {

    private String url;
    private String username;
    private String password;
    private String database;
    private String schema;
    private String warehouse;
}

package com.mna.springbootsecurity.datasource.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.multi-datasource")
@Getter
@Slf4j
public class MultiDataSourceConfig {
    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) log.info("Multi DataSource is Enabled!");
    }
}


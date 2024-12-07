package com.mna.springbootsecurity.base.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.cron")
@Getter
@Setter
public class CronProperties {

    @Data
    public static class Jwt {
        private String blacklistCleanup;
    }
}


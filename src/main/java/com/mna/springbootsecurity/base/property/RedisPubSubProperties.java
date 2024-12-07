package com.mna.springbootsecurity.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.pubsub")
@Getter
@Setter
public class RedisPubSubProperties {

    private String emailNotificationChannel;
    private String otpChannel;
}

package com.mna.springbootsecurity.mail.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EmailConfiguration {

    @Value("${application.services.mail.enabled}")
    private boolean emailServiceEnabled;

}

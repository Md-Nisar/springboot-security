package com.mna.springbootsecurity.logging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScheduledJobName {

    JWT_BLACKLISTED_TOKEN_CLEANUP("JWT Blacklisted Token Cleanup"),
    ANOTHER_JOB("Another Scheduled Job");
    // Add more job names as needed

    private final String jobName;
}


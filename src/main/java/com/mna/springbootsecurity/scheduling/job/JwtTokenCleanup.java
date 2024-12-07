package com.mna.springbootsecurity.scheduling.job;

import com.mna.springbootsecurity.cache.service.JwtTokenCacheService;
import com.mna.springbootsecurity.logging.annotation.LogScheduledJob;
import com.mna.springbootsecurity.logging.enums.ScheduledJobName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class JwtTokenCleanup {

    private final JwtTokenCacheService jwtTokenCacheService;

    @Scheduled(cron = "${application.cron.jwt.blacklist-cleanup}")
    @LogScheduledJob(jobName = ScheduledJobName.JWT_TOKEN_CLEANUP)
    public void clearExpiredBlacklistedTokens() {
        jwtTokenCacheService.clearExpiredBlacklistedTokens();
    }
}

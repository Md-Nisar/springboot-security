package com.mna.springbootsecurity.scheduling.jobs;

import com.mna.springbootsecurity.cache.service.JwtTokenCacheService;
import com.mna.springbootsecurity.distributed.lock.annotation.Lock;
import com.mna.springbootsecurity.distributed.base.enums.LockKey;
import com.mna.springbootsecurity.distributed.lock.LockManager;
import com.mna.springbootsecurity.logging.annotation.LogScheduledJob;
import com.mna.springbootsecurity.logging.enums.ScheduledJobName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class JwtTokenCleanupJob {

    private final JwtTokenCacheService jwtTokenCacheService;
    private final LockManager lockManager;

//    @Scheduled(cron = "${application.cron.jwt.blacklisted-token-cleanup}")
//    @LogScheduledJob(jobName = ScheduledJobName.JWT_BLACKLISTED_TOKEN_CLEANUP)
//    public void clearExpiredBlacklistedTokens() {
//        if (lockManager.tryLock(LockKey.CRON_JOB_LOCK, 120, TimeUnit.SECONDS)) {
//            try {
//                jwtTokenCacheService.clearExpiredBlacklistedTokens();
//            } finally {
//                lockManager.unlock(LockKey.CRON_JOB_LOCK);
//            }
//        }
//    }

    @Scheduled(cron = "${application.cron.jwt.blacklisted-token-cleanup}")
    @Lock(lockKey = LockKey.CRON_JOB_LOCK, leaseTime = 120, timeUnit = TimeUnit.SECONDS)
    @LogScheduledJob(jobName = ScheduledJobName.JWT_BLACKLISTED_TOKEN_CLEANUP)
    public void test() {
        try {
            Thread.sleep(10000);
            log.info("Testing lock annotation");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

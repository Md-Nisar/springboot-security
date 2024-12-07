package com.mna.springbootsecurity.logging.aspect;

import com.mna.springbootsecurity.logging.annotation.LogScheduledJob;
import com.mna.springbootsecurity.logging.enums.ScheduledJobName;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ScheduledJobLoggingAspect {

    @Around("@annotation(logScheduledJob)")
    public Object logScheduledJob(ProceedingJoinPoint joinPoint, LogScheduledJob logScheduledJob) throws Throwable {
        ScheduledJobName jobName = logScheduledJob.jobName();
        long startTime = System.currentTimeMillis();

        log.info("Cron job started:: {} at {}", jobName, startTime);

        try {
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            log.info("Cron job completed:: {} in {} ms", jobName, endTime - startTime);

            return result;
        } catch (Exception ex) {
            long errorTime = System.currentTimeMillis();
            log.error("Cron job failed:: {} at {} with error: {}", jobName, errorTime, ex.getMessage(), ex);
            throw ex;
        }
    }
}


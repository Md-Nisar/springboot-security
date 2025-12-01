package com.mna.springbootsecurity.distributed.lock.annotation;

import com.mna.springbootsecurity.distributed.base.enums.LockKey;
import com.mna.springbootsecurity.distributed.lock.LockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class LockAspect {

    private final LockManager lockManager;

    @Around("@annotation(lock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        LockKey lockKey = lock.lockKey();

        // Try to acquire the lock and proceed only if successful
        boolean acquired = lockManager.tryLock(lockKey, lock.leaseTime(), lock.timeUnit());

        if (!acquired) {
            return null; // Or throw an exception if skipping is not desired
        }

        try {
            return joinPoint.proceed();
        } finally {
            lockManager.unlock(lockKey);
        }
    }


}

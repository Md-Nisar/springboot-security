package com.mna.springbootsecurity.distributed.lock.impl;

import com.mna.springbootsecurity.distributed.base.enums.LockKey;
import com.mna.springbootsecurity.distributed.lock.LockManager;
import com.mna.springbootsecurity.distributed.util.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * RedisDistributedLockManager provides a Redis-based distributed locking mechanism.
 * This ensures that scheduled tasks or critical processes are executed only once across multiple instances.
 *
 * @author [Md Nisar Ahmed]
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDistributedLockManager implements LockManager {

    private final RedissonClient redissonClient;
    private static final String nodeName = NodeUtil.getNodeName();


    /**
     * Attempts to acquire a distributed lock.
     *
     * @param lockKey   The unique key for the lock.
     * @param leaseTime The duration after which the lock will be automatically released.
     * @param unit      The time unit for the lease duration.
     * @return {@code true} if the lock is acquired, otherwise {@code false}.
     */
    public boolean tryLock(LockKey lockKey, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey.getKey());
        try {
            log.debug("Attempting to acquire lock - '{}' for {} {}", lockKey.getKey(), leaseTime, unit);

            boolean acquired = false;
            if (!lock.isLocked()) {
                acquired = lock.tryLock(0, leaseTime, unit);
            }

            if (acquired) {
                log.debug("Successfully acquired lock - '{}'", lockKey.getKey());
            } else {
                log.warn("Failed to acquire lock - '{}'", lockKey.getKey());
            }
            return acquired;
        } catch (InterruptedException e) {
            log.error("Thread interrupted while acquiring lock - '{}'", lockKey.getKey(), e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Releases the distributed lock if held by the current thread.
     *
     * @param lockKey The unique key for the lock.
     */
    public void unlock(LockKey lockKey) {
        RLock lock = redissonClient.getLock(lockKey.getKey());
        if (lock.isHeldByCurrentThread()) {
            log.debug("Releasing lock - '{}'", lockKey.getKey());
            lock.unlock();
            log.debug("Successfully released lock - '{}'", lockKey.getKey());
        }
    }

}

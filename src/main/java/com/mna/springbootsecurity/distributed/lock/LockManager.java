package com.mna.springbootsecurity.distributed.lock;

import com.mna.springbootsecurity.distributed.base.enums.LockKey;

import java.util.concurrent.TimeUnit;

/**
 * LockManager Interface - Manages distributed locks in a multi-instance application.
 *
 * <p>
 * This interface provides a standard contract for acquiring and releasing locks
 * using a distributed locking mechanism (e.g., Redisson, Zookeeper, etc.).
 * </p>
 *
 * <p>
 * Implementations of this interface should ensure that locks are acquired and
 * released properly to avoid deadlocks and ensure data consistency.
 * </p>
 *
 * <p>
 * The locking mechanism is useful for:
 * <ul>
 *   <li>Preventing concurrent execution of scheduled jobs</li>
 *   <li>Ensuring consistency in database updates</li>
 *   <li>Preventing duplicate processing of requests</li>
 * </ul>
 * </p>
 */
public interface LockManager {

    /**
     * Attempts to acquire a distributed lock for a specified duration.
     *
     * <p>
     * This method tries to acquire a lock on the given {@link LockKey}.
     * If successful, the lock is held for the specified lease time and then automatically released.
     * </p>
     *
     * @param lockKey   The unique key identifying the lock (defined in {@link LockKey} enum).
     * @param leaseTime The duration for which the lock should be held.
     * @param unit      The time unit of the lease duration (e.g., seconds, minutes).
     * @return {@code true} if the lock was successfully acquired, otherwise {@code false}.
     */
    boolean tryLock(LockKey lockKey, long leaseTime, TimeUnit unit);

    /**
     * Releases a previously acquired distributed lock.
     *
     * <p>
     * This method ensures that only the thread that holds the lock can release it.
     * If the lock is not held by the current thread, it does nothing.
     * </p>
     *
     * @param lockKey The unique key identifying the lock (defined in {@link LockKey} enum).
     */
    void unlock(LockKey lockKey);
}

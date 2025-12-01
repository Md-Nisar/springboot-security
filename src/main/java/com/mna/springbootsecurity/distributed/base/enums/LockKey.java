package com.mna.springbootsecurity.distributed.base.enums;

import lombok.Getter;

/**
 * Enum representing different distributed lock keys used across the system.
 *
 * <p>
 * This enum centralizes all lock keys, making them easy to maintain and preventing
 * duplicate or inconsistent lock names across the codebase.
 * </p>
 */
@Getter
public enum LockKey {
    //  Scheduled Jobs
    CRON_JOB_LOCK("app:lock:cron:job"),
    DATA_SYNC_LOCK("lock:data:sync"),

    // User-related Locks
    USER_REGISTRATION_LOCK("lock:user:registration"),
    USER_LOGIN_LOCK("lock:user:login"),
    PASSWORD_RESET_LOCK("lock:user:password-reset"),

    // Payment and Transactions
    PAYMENT_PROCESSING_LOCK("lock:payment:processing"),
    TRANSACTION_LOCK("lock:transaction"),
    ORDER_PLACEMENT_LOCK("lock:order:placement"),

    //  Caching and Rate Limiting
    CACHE_REFRESH_LOCK("lock:cache:refresh"),
    RATE_LIMIT_LOCK("lock:rate:limit"),

    // Inventory Management
    STOCK_UPDATE_LOCK("lock:inventory:stock-update"),
    PRODUCT_AVAILABILITY_LOCK("lock:inventory:product-availability"),

    // Notifications and Messaging
    EMAIL_SENDING_LOCK("lock:notification:email"),
    SMS_SENDING_LOCK("lock:notification:sms"),
    PUSH_NOTIFICATION_LOCK("lock:notification:push");
    private final String key;

    LockKey(String key) {
        this.key = key;
    }
}

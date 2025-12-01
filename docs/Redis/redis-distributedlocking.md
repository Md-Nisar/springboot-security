# Distributed Locking in Spring Boot using Redis and Redisson

## Overview
Distributed locking is essential for ensuring **synchronization** in distributed systems where multiple instances of an application run concurrently. Redis, combined with **Redisson**, provides an efficient way to implement distributed locks in a Spring Boot application.

This guide covers:
- What is distributed locking?
- Why use Redis for distributed locks?
- Implementation using **Spring Boot and Redisson**.

---

## Why Distributed Locking?
In microservices or multi-instance environments, race conditions can occur when multiple services try to perform the same operation simultaneously. A distributed lock ensures that only **one instance executes the critical section** at a time, preventing duplicate processing.

Example use cases:
- **Scheduled Jobs**: Prevent multiple instances from executing the same scheduled task.
- **Transactional Operations**: Ensure a single execution of a database update.
- **Inventory Management**: Avoid overselling in an e-commerce platform.

---

## Why Redis for Locking?
Redis is widely used for distributed locks because:
- It provides **low latency** operations.
- Supports **automatic expiration** to prevent deadlocks.
- Works well in **clustered environments**.

---

## Setting Up Redisson in Spring Boot

### 1. Add Redisson Dependency
Include the following in your `pom.xml`:

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.22.0</version>
</dependency>
```

---

### 2. Configure Redisson Client
Create a `RedissonConfig` class to configure the Redis connection:

```java
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://localhost:6379")
              .setConnectionMinimumIdleSize(2);
        return Redisson.create(config);
    }
}
```

Replace `redis://localhost:6379` with your Redis instance address if running remotely.

---

### 3. Implementing Distributed Lock

#### Example: Preventing Duplicate Email Sending in a Scheduled Task

```java
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class EmailSchedulerService {
    
    private final RedissonClient redissonClient;
    private final EmailService emailService; // Custom email service

    public EmailSchedulerService(RedissonClient redissonClient, EmailService emailService) {
        this.redissonClient = redissonClient;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void sendDailyEmail() {
        RLock lock = redissonClient.getLock("dailyEmailLock");

        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) { 
                System.out.println("Lock acquired, sending email...");
                emailService.sendDailyEmails(); // Implement email logic
            } else {
                System.out.println("Another instance already acquired the lock.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released.");
            }
        }
    }
}
```

### Explanation:
- **`tryLock(0, 10, TimeUnit.MINUTES)`** → Tries to acquire the lock immediately and holds it for a maximum of **10 minutes**.
- **Ensures only one instance** of the application sends the email at 9 AM.
- **If another instance holds the lock**, the current instance skips execution.
- **Releases the lock after execution** to allow next-day execution.

---

## Best Practices
- **Set an appropriate `leaseTime`**: Avoid holding locks indefinitely to prevent deadlocks.
- **Use auto-renewal (`lock.lock()`)** if the task duration is unpredictable.
- **Ensure lock is always released** using `finally`.
- **Use unique lock names** for different tasks (`dailyEmailLock`, `orderProcessingLock`).

---

## Alternative Approaches
If Redis is not an option, consider:
- **Database-based locks** (`LOCK TABLE`, row-based locking mechanisms).
- **Quartz Scheduler with a clustered job store**.

---

## Conclusion
Using Redis and Redisson for distributed locking ensures that critical operations execute **only once** in a multi-instance Spring Boot environment. This approach is efficient, scalable, and prevents race conditions in distributed systems.

Would you like more details on alternative locking mechanisms? 🚀


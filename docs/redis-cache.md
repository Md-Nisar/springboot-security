
---

# Redis for Caching

This document provides guidelines for configuring Redis.

## Redis Configuration

### a. Setup

1. **Dependency Setup**: Ensure your project includes the necessary dependencies for Spring Boot and Redis integration.

2. **Configuration**: In your `application.properties` or `application.yml`, configure Redis connection details such as host, port, and database.

   Example:
   ```
   spring.redis.host=localhost
   spring.redis.port=6379
   spring.redis.database=0
   ```

### b. Profile Activation

Use profiles to activate Redis-specific configurations in your Spring Boot application.
 ```
   spring.profiles.active=redis
   ```


## 1. Redis for JWT Token Management

Redis facilitates efficient management of JWT tokens through structured storage:

### a. Key Structures in Redis

1. ``` Jwt:blacklist:<username> ```
    - **Data type**: SET.
    - **Purpose**: Stores blacklisted JWT tokens to prevent reuse.
    - **Usage**: Tokens are added upon logout or expiration to maintain security.

2. ``` jwt:access_token:<username> ```
    - **Data type**: KEY - VALUE.
    - **Purpose**: Stores JWT access tokens associated with user, organized by username.
    - **Usage**: Enables quick retrieval and validation of tokens during authentication and authorization processes.

## b. Steps/Documentation for JWT Token Storage and Blacklisting Mechanism
Documentation for JWT Token Storage and Blacklisting Mechanism

## 1. Introduction
This document describes the mechanism implemented to store JWT tokens upon user login, blacklist tokens upon user logout, and clean expired tokens from the system.

## 2. JWT Token Storage on Login

### Steps:
1. Generate a JWT token upon successful user authentication.
2. Store the JWT token in a Redis database using a unique key format (e.g., `jwt:access_token:{username}`).
3. Set an expiration time for the token in Redis to match the token's expiration.

### Details:
- **Key Format:** `jwt:access_token:{username}`
- **Expiry:** Should be set to the same duration as the token's validity period.

## 3. JWT Token Blacklisting on Logout

### Steps:
1. Retrieve the JWT token when a user logs out.
2. Add the token to a blacklist stored in Redis.
3. Set an expiration time for the blacklist entry to match the token's remaining validity period. (NA)

### Details:
- **Key Format for Blacklist:** `jwt:blacklist:<username>`
- **Expiry:** Remaining validity period of the token. (NA)

## 4. JWT Token Validation

### Steps:
1. For every request, check if the JWT token is present in the blacklist.
2. If the token is blacklisted, reject the request and return an appropriate error response.
3. If the token is not blacklisted, proceed with normal authentication and authorization checks.

## 5. Cleaning Expired Tokens

### Steps:
1. Implement a scheduled task to periodically check and remove expired tokens from the Redis database.
2. Use a cron expression or fixed delay to run this task at regular intervals.

### Details:
- **Scheduled Task:** Should be configured to run at least once a day.
- **Cron Expression:** Example - `@Scheduled(cron = "0 0 2 * * ?")` for daily cleanup at 2 AM night.

## 6. Error Handling

### Steps:
1. Implement exception handling to catch and log errors during token storage, blacklisting, and validation processes.
2. Ensure proper logging mechanisms are in place to track any issues and their resolutions.

## 7. Testing

### Steps:
1. Write unit tests for the methods responsible for storing, blacklisting, and cleaning tokens.
2. Write integration tests to ensure the entire mechanism works as expected in the application context.

## Conclusion

Integrating Redis with your Spring Boot application enhances token management by leveraging its speed and scalability. Utilize these guidelines to configure and utilize Redis effectively for secure JWT token handling.

---

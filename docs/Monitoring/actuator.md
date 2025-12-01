
---

# Spring Boot Actuator Documentation

## Overview

Spring Boot Actuator provides a range of built-in endpoints for monitoring and managing your application. These endpoints expose useful information about your application's health, metrics, environment, and more, making it easier to manage and monitor your Spring Boot applications.

## Key Features

1. **Health Checks**: Provides details about the application's health status.
2. **Metrics**: Exposes application metrics and statistics.
3. **Environment**: Displays properties and configurations.
4. **Thread Dump**: Provides a thread dump of the JVM.
5. **Loggers**: Allows dynamic modification of log levels.

## Dependencies

To use Actuator in your Spring Boot application, include the following dependency in your `pom.xml` file:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Configuration

### Enabling Endpoints

Actuator endpoints are disabled by default. To enable specific endpoints, configure them in the `application.properties` or `application.yml` file:

**In `application.properties`:**
```properties
management.endpoints.web.exposure.include=health,info,metrics
```

**In `application.yml`:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### Customizing Endpoints

You can customize endpoint behavior and security by modifying `application.properties` or `application.yml`:

- **Changing Endpoint Path**:
  ```properties
  management.endpoints.web.base-path=/manage
  ```

- **Securing Endpoints**: Configure security settings to restrict access to sensitive endpoints.

### Health Endpoint

The `/actuator/health` endpoint provides the current health status of the application. It checks various components like database connections, disk space, etc.

**Customizing Health Checks**:
You can define custom health indicators by implementing the `HealthIndicator` interface.

### Metrics Endpoint

The `/actuator/metrics` endpoint exposes metrics related to the application's performance, such as memory usage, garbage collection, and active threads.

**Example Metrics Available**:
- `jvm.memory.used`
- `http.server.requests`

**Custom Metrics**:
Define custom metrics using `MeterRegistry` to monitor specific aspects of your application.

### Environment Endpoint

The `/actuator/env` endpoint shows the current environment properties, including configuration properties and system environment variables.

### Thread Dump Endpoint

The `/actuator/threaddump` endpoint provides a thread dump of the JVM, useful for debugging performance issues.

### Loggers Endpoint

The `/actuator/loggers` endpoint allows you to view and modify the logging levels of various loggers in your application.

**Changing Log Levels**:
You can change log levels dynamically via this endpoint by sending a POST request with the desired log level.

## Access Control

It is crucial to secure Actuator endpoints to prevent unauthorized access, especially in production environments.

### Basic Authentication

You can secure Actuator endpoints using basic authentication by configuring Spring Security in your `application.properties` or `application.yml`:

**In `application.properties`:**
```properties
spring.security.user.name=admin
spring.security.user.password=admin123
```

**In `application.yml`:**
```yaml
spring:
  security:
    user:
      name: admin
      password: admin123
```

### Custom Security Configuration

For more complex security needs, you can customize the security configuration using Spring Security’s `WebSecurityConfigurerAdapter`.

## Conclusion

Spring Boot Actuator provides essential tools for managing and monitoring your Spring Boot applications. By configuring and securing these endpoints, you can gain valuable insights and maintain control over your application's health and performance.

---

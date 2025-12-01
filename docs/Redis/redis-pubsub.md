
---

# Spring Boot Redis Pub/Sub Implementation

## Overview

This document outlines the architecture and folder structure for implementing Redis Pub/Sub (Publish/Subscribe) in a Spring Boot application. It includes details on configuration, publishing and subscribing to messages, and how to interact with the application through REST endpoints.

## Architecture

The architecture consists of several components interacting with Redis for real-time messaging:

### Architecture Diagram

```
+------------------------+         +-------------------------+
|                        |         |                         |
| Spring Boot Application| <-----> |     Redis Server       |
|                        |         |                         |
+------------------------+         +-------------------------+
                |
                |  +------------------------+
                |  |                        |
                v  |                        |
      +------------------+    +-----------------------+
      |                  |    |                       |
      |   Controller     |    |    Publisher          |
      |                  |    |                       |
      +------------------+    +-----------------------+
                |                      |
                |                      |
                v                      v
      +------------------+    +-----------------------+
      |                  |    |                       |
      |  Redis Publisher |    |   Redis Subscriber    |
      |                  |    |                       |
      +------------------+    +-----------------------+
                |                      |
                |                      |
                v                      v
      +------------------+    +-----------------------+
      |                  |    |                       |
      |   Redis Config   |    |   Message Listener    |
      |                  |    |                       |
      +------------------+    +-----------------------+

```

### Components

1. **Spring Boot Application**:
    - **RedisConfig**: Configures Redis connection and message listener container.
    - **RedisPublisher**: Service for publishing messages to Redis channels.
    - **RedisSubscriber**: Service for subscribing to Redis channels and handling incoming messages.
    - **Controller**: REST API endpoints for interacting with the application.

2. **Redis Server**:
    - Manages Pub/Sub channels for message exchange.

## Folder Structure

```
src/
│
├── main/
│   ├── java/
│   │   └── com/
│   │       └── yourcompany/
│   │           └── yourapp/
│   │               ├── config/
│   │               │   └── RedisConfig.java
│   │               ├── controller/
│   │               │   └── TestController.java
│   │               ├── publisher/
│   │               │   └── RedisPublisher.java
│   │               ├── subscriber/
│   │               │   └── RedisSubscriber.java
│   │               └── YourApplication.java
│   ├── resources/
│   │   ├── application.properties
│   │   └── application.yml
│   └── webapp/
│
├── test/
│   ├── java/
│   │   └── com/
│   │       └── yourcompany/
│   │           └── yourapp/
│   │               ├── controller/
│   │               │   └── TestControllerTests.java
│   │               ├── publisher/
│   │               │   └── RedisPublisherTests.java
│   │               ├── subscriber/
│   │               │   └── RedisSubscriberTests.java
│   │               └── YourApplicationTests.java
│   └── resources/
│
└── pom.xml
```

### Component Details

#### 1. **Main Application**

`YourApplication.java` - The main entry point of your Spring Boot application.

```java
package com.yourcompany.yourapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

#### 2. **Configuration**

`RedisConfig.java` - Configures Redis connection and message listeners.

```java
package com.yourcompany.yourapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.yourcompany.yourapp.subscriber.RedisSubscriber;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber);
    }

    @Bean
    public RedisSubscriber redisSubscriber() {
        return new RedisSubscriber();
    }
}
```

#### 3. **Publisher**

`RedisPublisher.java` - Service to publish messages to Redis channels.

```java
package com.yourcompany.yourapp.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
```

#### 4. **Subscriber**

`RedisSubscriber.java` - Handles incoming messages from Redis channels.

```java
package com.yourcompany.yourapp.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        System.out.println("Received message: " + messageBody + " from channel: " + channel);
    }
}
```

#### 5. **Controller**

`TestController.java` - REST API to interact with the publisher.

```java
package com.yourcompany.yourapp.controller;

import com.yourcompany.yourapp.publisher.RedisPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RedisPublisher redisPublisher;

    @Autowired
    public TestController(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @GetMapping("/publish")
    public String publish(@RequestParam String message) {
        redisPublisher.publish("my-channel", message);
        return "Message published!";
    }
}
```

#### 6. **Application Properties**

`application.properties` or `application.yml` - Configuration for Redis and other settings.

```properties
# application.properties
spring.redis.host=localhost
spring.redis.port=6379
```

### Testing

- **Unit Tests**: Create tests for each component in the `test` directory to ensure functionality and integration.
- **Integration Tests**: Use Spring Boot test annotations to verify interactions between components.

### References

- **Redis Documentation**: [Redis Pub/Sub Documentation](https://redis.io/topics/pubsub)
- **Spring Boot Documentation**: [Spring Boot Redis Documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/#)

---

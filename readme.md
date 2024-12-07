# Springboot Security


## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Core Implementation](#core-implementation)
- [Redis Configuration](#redis-configuration)


## Introduction
  This document provides step-by-step instructions for setting up Spring Security in a Spring Boot application.

## Prerequisites

- Java 17
- SpiSpring Boot
- Docker (for running Redis)
- Maven 3.6.1

## Project Setup

1. **Create a Spring Boot application**:
   - You can use Spring Initializer to bootstrap your project with the necessary dependencies:
      - spring-boot-starter-web
      - spring-boot-starter-data-jpa
      - spring-boot-starter-security
      - spring-security-test
      - spring-boot-starter-validation
      - mysql-connector-j
      - jjwt-api, jjwt-impl, jjwt-jackson

## Core Implementation

### 1. Create a Security Configuration Class
1. Create a `SecurityConfiguration` class and annotate it with `@EnableWebSecurity`.
2. Define a `securityFilterChain` bean to configure HTTP security:
    - Allow public access to specific URLs.
    - Authenticate all other requests.
    - Set up role-based authorization.
    - Register filters
    - Customize logout (if necessary)

### 2. Create a Auth Configuration Class
1. Configure `UserDetailsService`:
    - Define a `UserDetailsService` bean to load user-specific data from the database.
    - Implement the `loadUserByUsername` method to fetch user details using `UserDao`.

2. Configure Password Encoder:
    - Define a `PasswordEncoder` bean to handle password encoding.
    - Use `BCryptPasswordEncoder` for secure password encoding.

3. Set Up DAO Authentication Provider:
    - Define a `DaoAuthenticationProvider` bean.
    - Set the `UserDetailsService` and `PasswordEncoder` for the provider.

4. Configure Authentication Manager:
    - Define an `AuthenticationManager` bean.
    - Retrieve the `AuthenticationManager` from the `AuthenticationConfiguration`.

### 3. Configure CORS
1. Create `WebConfiguration` Class:
2. Create a `WebMvcConfigurer` bean to configure CORS settings.
3. Configure CORS Mappings:
   - Override the `addCorsMappings` method in the `WebMvcConfigurer` bean.
   - Set up the CORS mappings to allow specific origins, methods, headers, and credentials.

### 4. Configure JWT Authentication
1. Add dependencies for JWT: `jjwt-api`, `jjwt-impl`, `jjwt-jackson`.
2. Create `JwtUtil` utility class for JWT token generation and validation.
3. Create filters:
   - Authorization Filter (`JwtAuthorizationFilter`): Validates the JWT token for every subsequent request after login.
4. Register the filters in the `securityConfiguration` class.

### 5. Implement APIs for Signup and Login
1. Create an entity for User.
2. Create DTOs for signup and login requests and responses.
3. Create controllers, Services, Repository for handling signup and login.
4. Implement methods to create a new user and authenticate an existing user.

### 6. Create Role Management
1. Create an entity for Role.
2. Set up a many-to-many relationship between users and roles.
3. Initialize default roles on application startup.

## 10. Test the Configuration
1. Test the security configuration using Postman or similar tools.
2. Verify that endpoints are secured as expected.
3. Ensure that signup, login, and JWT functionalities work correctly.

## 11. Additional Security Configurations
1. Configure CORS if needed.
2. Implement custom error handling for security-related exceptions.
3. Configure session management as per application requirements.

## 12. Documentation and Maintenance
1. Document the security setup and configurations for future reference.
2. Regularly review and update security configurations to address new threats.

---
## References
### 1. Docs
1. https://spring.io/projects/spring-security
2. https://jwt.io/
3. https://backendstory.com/spring-security-authentication-architecture-explained-in-depth/

### 2. Youtube
1. https://youtu.be/her_7pa0vrg?si=l2ocYcBoYQoKkNAj
2. https://youtu.be/KxqlJblhzfI?si=SEv0sTSIA7JpAK2a
3. https://youtu.be/tWcqSIQr6Ks?si=TcQnyhnReheH_YqG
4. https://youtu.be/y9NhIWN9ZK8?si=XLeLHrVlx2r6Z26-

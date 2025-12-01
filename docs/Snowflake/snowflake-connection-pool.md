# Snowflake DataSource Configuration and Connection Pooling

This document explains the configuration and setup for Snowflake database connection pooling using `BasicDataSource` in a Spring Boot application. It includes recommended properties and values for managing database connections effectively.

## Table of Contents
- [Introduction](#introduction)
- [Configuration Details](#configuration-details)
  - [Basic Connection Pooling](#basic-connection-pooling)
  - [Connection Validation](#connection-validation)
  - [Timeout and Eviction Settings](#timeout-and-eviction-settings)
  - [Abandoned Connections](#abandoned-connections)
- [Properties](#properties)
- [Conclusion](#conclusion)

## Introduction

This setup leverages the `BasicDataSource` class from Apache Commons DBCP to create and manage a connection pool to the Snowflake database. The configuration ensures efficient connection management with validation, timeouts, and eviction strategies.

The following are the key properties and configurations used for managing the connection pool:

## Configuration Details

### Basic Connection Pooling

1. **`setInitialSize(int size)`**  
   - **Description**: Defines the number of connections initialized at pool creation.
   - **Recommended Value**: `5`
   - **Explanation**: This provides a reasonable starting number of connections to handle initial requests efficiently.

2. **`setMaxTotal(int maxTotal)`**  
   - **Description**: Defines the maximum number of connections that the pool can maintain.
   - **Recommended Value**: `20`
   - **Explanation**: For moderate traffic, `20` connections should be sufficient. Adjust based on your application's traffic.

3. **`setMinIdle(int minIdle)`**  
   - **Description**: The minimum number of idle connections that the pool should maintain.
   - **Recommended Value**: `5`
   - **Explanation**: Keeping idle connections ready for quick use without overloading the database is crucial for performance.

4. **`setMaxIdle(int maxIdle)`**  
   - **Description**: The maximum number of idle connections to keep in the pool.
   - **Recommended Value**: `10`
   - **Explanation**: Limits the number of idle connections to prevent resource wastage.

### Connection Validation

1. **`setTestOnBorrow(true)`**  
   - **Description**: Ensures that each connection is validated before it is borrowed from the pool.
   - **Recommended Value**: `true`
   - **Explanation**: This setting helps avoid issues with invalid or broken connections being used by the application.

2. **`setTestOnReturn(false)`**  
   - **Description**: Ensures that connections are not tested when they are returned to the pool.
   - **Recommended Value**: `false`
   - **Explanation**: Testing connections when they are returned typically provides no additional benefit and increases overhead.

3. **`setTestWhileIdle(true)`**  
   - **Description**: Validates idle connections periodically to prevent using stale connections.
   - **Recommended Value**: `true`
   - **Explanation**: Helps ensure that connections that have been idle for a while are still valid before being reused.

4. **`setValidationQuery("SELECT 1")`**  
   - **Description**: A simple query used to validate that a connection is still valid.
   - **Recommended Value**: `"SELECT 1"`
   - **Explanation**: This lightweight query is sufficient for most databases, including Snowflake, to test the connection.

### Timeout and Eviction Settings

1. **`setMaxWaitMillis(long millis)`**  
   - **Description**: Defines how long the pool will wait for a connection to become available.
   - **Recommended Value**: `10000` (10 seconds)
   - **Explanation**: A 10-second timeout is typical. If your application needs to wait longer for a connection, adjust this value accordingly.

2. **`setTimeBetweenEvictionRunsMillis(long millis)`**  
   - **Description**: The interval between background tasks that check and remove idle connections.
   - **Recommended Value**: `300000` (5 minutes)
   - **Explanation**: A 5-minute interval helps in cleaning up old connections that might have been idle for too long.

3. **`setMinEvictableIdleTimeMillis(long millis)`**  
   - **Description**: Specifies the minimum amount of time an idle connection can remain in the pool before being evicted.
   - **Recommended Value**: `600000` (10 minutes)
   - **Explanation**: A 10-minute idle time ensures that unused connections are removed from the pool to prevent resource wastage.

4. **`setNumTestsPerEvictionRun(int numTests)`**  
   - **Description**: Specifies how many connections are validated during each eviction run.
   - **Recommended Value**: `3`
   - **Explanation**: Validating a small number of connections per run (e.g., `3`) ensures efficient background cleanup without excessive overhead.

### Abandoned Connections

1. **`setRemoveAbandonedOnMaintenance(true)`**  
   - **Description**: Enables removal of abandoned connections during maintenance tasks.
   - **Recommended Value**: `true`
   - **Explanation**: Helps ensure that abandoned connections (e.g., not properly closed) are cleaned up to prevent resource leakage.

2. **`setRemoveAbandonedTimeout(int seconds)`**  
   - **Description**: Specifies the timeout after which a connection is considered abandoned if not closed.
   - **Recommended Value**: `300` (5 minutes)
   - **Explanation**: If a connection has been idle for more than 5 minutes without being properly closed, it will be removed.

3. **`setLogAbandoned(true)`**  
   - **Description**: Enables logging of abandoned connections for troubleshooting purposes.
   - **Recommended Value**: `true`
   - **Explanation**: Helps identify and troubleshoot issues where connections are not properly closed in the application code.

## Properties

In addition to connection pooling and validation settings, make sure to configure the following in your `application.properties` or `application.yml` file:

```properties
application.snowflake.url=jdbc:snowflake://<snowflake_host>/?
application.snowflake.username=<username>
application.snowflake.password=<password>
application.snowflake.driver-class-name=net.snowflake.client.jdbc.SnowflakeDriver

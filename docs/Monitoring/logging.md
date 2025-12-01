# LogLevelController Documentation

## Overview
The `LogLevelController` allows dynamic modification of logging levels at runtime without restarting the application. This is useful for debugging issues in a production environment without affecting overall system stability.

## Endpoint Details

### Change Log Level
**URL:** `/api/v1/system/logging/{loggerName}/{level}`  
**Method:** `POST`  
**Description:** Updates the log level of a specific logger dynamically.

#### Path Variables:
- `loggerName` (String) - The name of the logger (e.g., `com.example.service`).
- `level` (String) - The desired log level. Allowed values: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`.

#### Example Request:
```sh
curl -X POST "http://localhost:8080/api/v1/system/logging/com.example/DEBUG"
```

#### Example Response:
```
Log level for com.example changed to DEBUG
```

#### Error Response:
If an invalid log level is provided:
```
Invalid log level. Allowed values: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF
```

## Alternative: Using Spring Boot Actuator
Instead of implementing a custom controller, Spring Boot provides a built-in way to change log levels dynamically using the **Actuator**.

### Enable Actuator Logging Endpoint
Add the following property to `application.properties`:
```properties
management.endpoints.web.exposure.include=loggers
```

### Change Log Level Using Actuator
You can use the Actuator API to change the log level dynamically:

#### Get Current Log Level:
```sh
curl -X GET http://localhost:8080/actuator/loggers/com.example
```

#### Set Log Level to DEBUG:
```sh
curl -X POST http://localhost:8080/actuator/loggers/com.example \
     -H "Content-Type: application/json" \
     -d '{"configuredLevel": "DEBUG"}'
```

### Why Use a Custom Controller Instead of Actuator?
| Feature | Actuator | Custom Controller |
|---------|---------|------------------|
| **Ease of Use** | Requires enabling endpoints | Directly accessible |
| **Security** | Requires securing Actuator | Can be restricted to admin users |
| **Flexibility** | Standard API, no customization | Can be modified as needed |

## Security Considerations
- Ensure this endpoint is **secured** to prevent unauthorized users from modifying log levels.
- Consider implementing role-based access control (RBAC) using **Spring Security**:

```java
@PreAuthorize("hasRole('SYSTEM')")
@PostMapping("/logging/{loggerName}/{level}")
```

## Conclusion
This controller provides a simple yet powerful way to dynamically change log levels in a Spring Boot application. If security and flexibility are required, a custom controller is recommended. If a standard approach is preferred, the **Spring Boot Actuator** can be used instead.


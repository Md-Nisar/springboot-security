
---

# Implementing Swagger with Spring Docs OpenAPI

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 17 or higher installed.
2. **Spring Boot Application**: You should have a Spring Boot application set up.

## Steps

### 1. Add Dependencies

- Add the necessary dependencies for Spring Docs OpenAPI and Swagger to your `pom.xml` (for Maven) or `build.gradle` (for Gradle) file.

**For Maven:**

```xml
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### 2. Configure Swagger

- Create a ```OpenApiConfiguration```configuration class in your Spring Boot application to set up Swagger using Spring Docs OpenAPI. This class should annotate your configuration with `@Configuration` and use `@OpenAPIDefinition` (if using declarative approach) to provide metadata.

### 3. Annotate Your API or Configure Programmatically

#### 1.Declarative approach
- Use annotations from Spring Docs OpenAPI to describe your API. For example:
    - `@OpenAPIDefinition`: Provides general API information.
    - `@Info`: Defines the title, description, and version of your API.
    - `@Tag`: Groups endpoints by tags.
    - `@Operation`: Describes individual API operations.


#### 2. Programmatic approach
- Define OpenAPI bean and configure. For example:
  - `Info`: Provides general API information.
  - `Components`: Defines the title, description, and version of your API.
  - `Add Security Item`: Groups endpoints by tags.

#### 3. Basic Configuration using```application.properties```
```
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

### 4. Run Your Application

- Start your Spring Boot application to ensure that the Swagger documentation is generated and accessible.

### 5. Access Swagger UI

- Open your browser and navigate to the Swagger UI endpoint, typically found at:
    - `http://localhost:8081/swagger-ui/index.html`
    - `http://localhost:8081/v3/api-docs`

### 6. Verify Documentation

- Verify that your API endpoints, models, and descriptions are correctly displayed in the Swagger UI. Ensure that all endpoints are documented as expected.

### 7. Customize Documentation (Optional)

- Customize the Swagger documentation further by modifying the configuration class and annotations. You can adjust descriptions, add additional metadata, or configure specific details for your API documentation.

### 8. Update Documentation Regularly

- Keep your Swagger documentation up-to-date with changes in your API endpoints and models to ensure it accurately reflects the current state of your API.

## References

- [Springdoc OpenAPI Documentation](https://springdoc.org/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

---
```markdown
# Resolving CORS Issues Between Angular and Spring Boot

## Introduction

When working with a frontend application (such as Angular) hosted on one domain and a backend (like Spring Boot) hosted on another, you may encounter a **CORS (Cross-Origin Resource Sharing)** error. This happens because browsers enforce security policies that block requests made from one origin to another unless the server explicitly allows it.

For example, let's assume your setup looks like this:

- **Frontend (Angular)**: http://frontend.example.com (Hosted on Nginx)
- **Backend (Spring Boot)**: http://backend.example.com

Since these two services are on different origins, when the frontend tries to communicate with the backend (for API calls), the browser will block the request unless we configure CORS properly.

## What is CORS?

CORS stands for **Cross-Origin Resource Sharing**. It is a mechanism that allows web applications running at one origin (e.g., `http://frontend.example.com`) to make requests to a different origin (e.g., `http://backend.example.com`). 

If CORS is not configured, the browser will block the request and throw a CORS error.

## Example of CORS Error

If your Angular application tries to call the Spring Boot API, you might see an error like this in the browser console:

```
Access to XMLHttpRequest at 'http://backend.example.com/api/data' from origin 'http://frontend.example.com' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

This error indicates that the server (`http://backend.example.com`) has not allowed the browser to access its resources from the `http://frontend.example.com` origin.

## Configuring CORS in Spring Boot

To resolve the CORS issue, we need to configure the backend (Spring Boot) to allow cross-origin requests from the frontend (Angular app). Here's how to do it.

### Method 1: Using `@CrossOrigin` Annotation at the Controller Level

You can allow CORS for specific endpoints by using the `@CrossOrigin` annotation in your controller:

```java
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://frontend.example.com")
@RestController
public class MyController {

    @GetMapping("/api/data")
    public String getData() {
        return "Data from backend";
    }
}
```

This allows the `http://frontend.example.com` origin to access the `/api/data` endpoint in the Spring Boot backend.

### Method 2: Global CORS Configuration

To allow CORS globally for all endpoints, you can configure it in the Spring Boot application's configuration class:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply to all paths
                .allowedOrigins("http://frontend.example.com")  // Allow this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow these HTTP methods
                .allowCredentials(true);  // Allow cookies/credentials
    }
}
```

This will configure the Spring Boot application to allow cross-origin requests for all paths and methods (`GET`, `POST`, `PUT`, `DELETE`) from the `http://frontend.example.com` origin.

### Method 3: Spring Security CORS Configuration (Optional)

If you're using Spring Security, you'll need to configure CORS in the security configuration as well:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors()  // Enable CORS in security
            .and()
            .csrf().disable()  // Disable CSRF for simplicity (enable in production)
            .authorizeRequests()
            .anyRequest().authenticated();
        return http.build();
    }
}
```

You'll also need to define a CORS configuration source:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("http://frontend.example.com");
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}
```

## How It Works

When your Angular app tries to make a request to the Spring Boot backend, the browser sends a **preflight request** (an `OPTIONS` request) to check if the CORS policy allows the cross-origin request.

The Spring Boot backend responds with CORS headers such as:

```
Access-Control-Allow-Origin: http://frontend.example.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Credentials: true
```

If the headers are present and match the frontend's origin, the browser allows the actual request to go through.

## Testing the Solution

1. Deploy your Angular app on the Nginx server at `http://frontend.example.com`.
2. Deploy your Spring Boot backend on `http://backend.example.com`.
3. Ensure CORS is properly configured in the Spring Boot backend (either through `@CrossOrigin` or global configuration).
4. Open your browser's developer console and check that no CORS error is present when your frontend makes requests to the backend.

## Conclusion

By properly configuring CORS in the Spring Boot backend, you allow your Angular app hosted on a different domain to communicate with the backend, ensuring a smooth interaction between the two.

If you encounter any issues, ensure that:
- The allowed origin is set correctly.
- The HTTP methods are permitted (GET, POST, etc.).
- Cookies/credentials are handled properly if required.

Happy coding!
```
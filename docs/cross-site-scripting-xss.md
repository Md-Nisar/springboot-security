# Defending Against XSS in Java/Spring Boot

## Overview

Cross-Site Scripting (XSS) is a security vulnerability that allows attackers to inject malicious scripts into web pages. XSS vulnerabilities can lead to session hijacking, stolen credentials, and unauthorized actions. This guide provides best practices, standards, and tools for protecting your Java/Spring Boot application from XSS attacks.

## Table of Contents
1. [Understanding XSS](#understanding-xss)
2. [Key Strategies for Preventing XSS](#key-strategies-for-preventing-xss)
   - [Input Validation](#input-validation)
   - [Output Encoding](#output-encoding)
   - [Using Templating Engines Safely](#using-templating-engines-safely)
   - [HTML Sanitization](#html-sanitization)
   - [Filter Request Input](#filter-request-input)
   - [Secure JavaScript Usage](#secure-javascript-usage)
3. [Spring Boot Security Mechanisms](#spring-boot-security-mechanisms)
   - [Content Security Policy (CSP)](#content-security-policy-csp)
   - [X-XSS-Protection Header](#x-xss-protection-header)
4. [Best Practices and Standards](#best-practices-and-standards)
5. [Tools and Libraries](#tools-and-libraries)
6. [Conclusion](#conclusion)

---

## Understanding XSS

There are three main types of XSS:
- **Reflected XSS**: Malicious scripts are injected and immediately reflected back to the user.
- **Stored XSS**: Malicious scripts are stored on the server and executed when other users load the affected page.
- **DOM-based XSS**: Vulnerabilities exist in the client-side code (JavaScript) rather than on the server.

---

## Key Strategies for Preventing XSS

### Input Validation
Always validate incoming data to ensure it conforms to expected formats:
```java
@NotBlank
@Size(min = 5, max = 255)
private String username;
```

### Output Encoding
Encode user input when rendering it to prevent malicious code from being executed.

- **Spring `HtmlUtils`**:
  ```java
  import org.springframework.web.util.HtmlUtils;
  String safeString = HtmlUtils.htmlEscape(unsafeInput);
  ```

- **OWASP Java Encoder**:
  ```java
  import org.owasp.encoder.Encode;
  String safeString = Encode.forHtml(unsafeInput);
  ```

#### Contextual Encoding
Use encoding based on the context:
- **HTML Content**: `Encode.forHtml()`
- **JavaScript Context**: `Encode.forJavaScript()`
- **CSS Context**: `Encode.forCssString()`

### Using Templating Engines Safely

#### Thymeleaf
Thymeleaf escapes output by default:
```html
<p th:text="${userInput}">User input</p> <!-- Automatically escaped -->
```

#### JSP
Use JSTL or `c:out` to automatically escape output:
```jsp
<c:out value="${userInput}"/>
```

### HTML Sanitization
For rich text input, sanitize user-generated HTML using **Jsoup**:
```java
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
String safeHTML = Jsoup.clean(unsafeHTML, Whitelist.basic());
```

### Filter Request Input
Use `HttpServletRequestWrapper` to filter and sanitize incoming requests:
```java
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return sanitize(value);
    }
    private String sanitize(String value) {
        return Jsoup.clean(value, Whitelist.none());
    }
}
```

### Secure JavaScript Usage
Avoid inserting untrusted data directly into JavaScript. Always encode it properly:
```html
<script>var username = [[${userInput}]];</script> <!-- Escapes content -->
```

---

## Spring Boot Security Mechanisms

### Content Security Policy (CSP)
Configure a **Content Security Policy (CSP)** to restrict content types:
```java
http
    .headers()
    .contentSecurityPolicy("script-src 'self'");
```

### X-XSS-Protection Header
Enable the `X-XSS-Protection` header to block potential XSS attacks:
```java
http
    .headers()
    .xssProtection()
    .block(true);
```

---

## Best Practices and Standards
1. **Input Validation**: Restrict inputs to expected formats.
2. **Output Encoding**: Use proper output encoding for all user-generated data.
3. **Security Headers**: Configure security headers (CSP, X-XSS-Protection).
4. **Sanitize HTML**: Use libraries like **Jsoup** for HTML sanitization.
5. **Context-Aware Encoding**: Use appropriate encoding based on where the data is being rendered (HTML, JavaScript, CSS).
6. **Regular Security Testing**: Use automated security scanners (e.g., **OWASP ZAP**) to detect vulnerabilities.
7. **Keep Dependencies Updated**: Ensure that third-party libraries (Spring, OWASP, etc.) are up-to-date.
8. **Developer Awareness**: Educate developers about XSS and other security vulnerabilities.

---

## Tools and Libraries

- **OWASP Java Encoder**: Context-aware encoding for HTML, JavaScript, and CSS.
- **Jsoup**: HTML sanitization library.
- **Spring Security**: For enabling headers and security policies.
- **Thymeleaf/JSP**: Templating engines with built-in encoding support.
- **OWASP ZAP/Burp Suite**: Automated tools for vulnerability scanning and testing.

---

## Conclusion

Defending against XSS in Java/Spring Boot applications requires a multi-faceted approach, including input validation, output encoding, secure templating, and proper sanitization. Leveraging available libraries such as OWASP Java Encoder, Jsoup, and Spring Security can help mitigate XSS vulnerabilities effectively. Regularly update your libraries, educate your developers, and scan your application for potential security gaps.

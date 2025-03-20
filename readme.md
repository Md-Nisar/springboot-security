# Spring Boot Security Project

## Project Structure

```
springboot-security/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mna/
│   │   │           └── springbootsecurity/
│   │   │               ├── base/
│   │   │               ├── controller/
│   │   │               ├── domain/
│   │   │               ├── security/
│   │   │               ├── service/
│   │   │               └── util/
│   │   ├── resources/
│   │       ├── application.properties
│   │       └── ...
│   ├── test/
│       └── java/
│           └── com/
│               └── mna/
│                   └── springbootsecurity/
│                       └── ...
├── docs/
│   ├── Architecture/
│   │   └── Diagrams/
│   ├── Monitoring/
│   └── readme.md
├── target/
├── pom.xml
└── readme.md
```

## Folder Structure Explained

- **src/main/java/com/mna/springbootsecurity/**: Contains the main Java source code.
  - **base/**: Contains base classes and constants.
  - **controller/**: Contains REST controllers.
  - **domain/**: Contains domain models and repositories.
  - **security/**: Contains security configurations and utilities.
  - **service/**: Contains service layer classes.
  - **util/**: Contains utility classes.

- **src/main/resources/**: Contains application configuration files.
  - **application.properties**: Main configuration file for the Spring Boot application.

- **src/test/java/com/mna/springbootsecurity/**: Contains test classes.

- **docs/**: Contains project documentation.
  - **Architecture/Diagrams/**: Contains architecture diagrams.
  - **Monitoring/**: Contains monitoring-related documentation.
  - **readme.md**: Main documentation file.

- **target/**: Contains compiled classes and build artifacts.

- **pom.xml**: Maven configuration file.

- **readme.md**: Project overview and instructions.

## Important Files in Root Directory

- **pom.xml**: Maven configuration file that manages project dependencies and build configuration.
- **Dockerfile**: Instructions to build a Docker image for the application.
- **.gitignore**: Specifies files and directories to be ignored by Git.
- **README.md**: Project overview, instructions, and documentation.

## How to Build

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/springboot-security.git
   cd springboot-security
   ```

2. **Build the project using Maven:**
   ```sh
   mvn clean install
   ```

## How to Run

1. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

2. **Access the application:**
   - API: `http://localhost:8089/api/v1`
   - Swagger UI: `http://localhost:8089/swagger-ui.html`

## Version

- **Current Version:** 1.0.0
- **Java Version:** 17
- **Spring Boot Version:** 3.3.1

## Additional Information

- **Database:** MySQL
- **Cache:** Redis
- **Documentation:** Swagger/OpenAPI
- **Security:** Spring Security with JWT

For more details, refer to the documentation in the `docs/` directory.

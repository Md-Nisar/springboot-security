
# **Spring Boot Application with MySQL and Redis using Docker**

This repository contains a Spring Boot application that uses **MySQL** for data storage and **Redis** for caching. It is containerized using **Docker** and orchestrated with **Docker Compose**.

---

## **Table of Contents**
1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [Dockerfile](#dockerfile)
4. [docker-compose.yml](#docker-composeyml)
5. [Setup and Build](#setup-and-build)
6. [Running the Application](#running-the-application)
7. [Accessing Services](#accessing-services)
8. [Optional Enhancements](#optional-enhancements)
9. [Stopping and Removing Services](#stopping-and-removing-services)
10. [Pushing Docker Images](#pushing-docker-images)

---

## **Prerequisites**

Before you begin, ensure you have the following installed:
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)
- **Maven** (for building Spring Boot application): [Install Maven](https://maven.apache.org/install.html)

---

## **Project Structure**

```
springboot-security/
├── Dockerfile
├── docker-compose.yml
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   ├── ...
├── pom.xml
```

- **Dockerfile**: Defines the image for the Spring Boot application.
- **docker-compose.yml**: Orchestrates Spring Boot, MySQL, and Redis containers.
- **src**: Contains the Java code and configuration for the Spring Boot application.
- **application.properties**: Contains MySQL and Redis configurations for the Spring Boot app.
- **pom.xml**: Maven configuration for the Spring Boot project.

---

## **Dockerfile**

The `Dockerfile` defines how to build the Docker image for the Spring Boot application.

```dockerfile
# Use a base image with JDK
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR
COPY target/springboot-security-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8089

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## **docker-compose.yml**

The `docker-compose.yml` file defines how to run the Spring Boot application along with MySQL and Redis containers.

```yaml
version: '3.9'

services:
  springboot-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: springboot-app
    depends_on:
      - mysql
      - redis
    ports:
      - "8089:8089"
    networks:
      - app-network

  mysql:
    image: mysql:8.0
    container_name: mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: spring_security
    ports:
      - "3306:3306"
    networks:
      - app-network

  redis:
    image: redis:latest
    container_name: redis
    restart: always
    ports:
      - "6379:6379"
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```

- **springboot-app**: Builds the Spring Boot application from the `Dockerfile`.
- **mysql**: Runs MySQL with custom environment variables.
- **redis**: Runs Redis.

---

## **Setup and Build**

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Md-Nisar/springboot-security.git
   cd springboot-security
   ```

2. **Build the Spring Boot application**:
   Run the following Maven command to package the Spring Boot application into a JAR file:
   ```bash
   mvn clean package
   ```

   This will generate a `.jar` file (e.g., `springboot-security-0.0.1-SNAPSHOT.jar`) inside the `target/` directory.

---

## **Running the Application**

1. **Build the Docker images**:
   Use Docker Compose to build the images for all services (Spring Boot, MySQL, and Redis):
   ```bash
   docker-compose build
   ```

2. **Start all services**:
   Start the containers (Spring Boot, MySQL, and Redis) by running:
   ```bash
   docker-compose up
   ```

    - The Spring Boot application will be available at [http://localhost:8089](http://localhost:8089).
    - MySQL and Redis services will be running on their respective ports:
        - MySQL: `localhost:3306`
        - Redis: `localhost:6379`

---

## **Accessing Services**

1. **Access the Spring Boot Application**:
   Open a web browser and visit [http://localhost:8089](http://localhost:8089) to interact with your application.

2. **Access MySQL**:
   To connect to MySQL using a MySQL client:
   ```bash
   mysql -h 127.0.0.1 -P 3306 -u root -p
   ```
   Enter the password `root`.

3. **Access Redis**:
   To connect to Redis:
   ```bash
   redis-cli -h 127.0.0.1 -p 6379
   PING
   ```
   You should get a response of `PONG`.

---

## **Optional Enhancements**

1. **Persist Data**:
   To persist data across container restarts, use Docker volumes for MySQL and Redis:

   Update `docker-compose.yml`:
   ```yaml
   mysql:
     volumes:
       - mysql-data:/var/lib/mysql

   redis:
     volumes:
       - redis-data:/data

   volumes:
     mysql-data:
     redis-data:
   ```

2. **Check Application Logs**:
   You can check the logs for the Spring Boot application:
   ```bash
   docker logs springboot-app
   ```

3. **Access Container Shell**:
   If you want to debug or interact with the containers, you can access their shell:
   ```bash
   docker exec -it springboot-app /bin/bash
   ```

---

## **Stopping and Removing Services**

To stop the containers and remove them:

```bash
docker-compose down
```

This will stop and remove all containers defined in the `docker-compose.yml` file.

---

## **Pushing Docker Images**

To push your Docker image to a registry like Docker Hub, follow these steps:

1. **Tag the Docker image**:
   ```bash
   docker tag springboot-app <your-dockerhub-username>/springboot-app
   ```

2. **Push the Docker image**:
   ```bash
   docker push <your-dockerhub-username>/springboot-app
   ```

---

## **Conclusion**

You have successfully Dockerized your Spring Boot application along with MySQL and Redis. You can now:
- Build and run your application in any environment with Docker.
- Easily manage multiple services using Docker Compose.

---

Let me know if you need any further customization!
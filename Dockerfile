# 1. Specify the base image
FROM openjdk:17-jdk-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the application JAR file into the container
COPY target/springboot-security-0.0.1-SNAPSHOT.jar app.jar

# 4. Expose the application port to the host
EXPOSE 8089

# 5. Define the command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]

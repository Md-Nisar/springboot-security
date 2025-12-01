#!/bin/bash

# Variables
APP_DIR="D:/Projects/SPRING-BOOT/springboot-security" # Replace with your application's path
BUILD_TOOL="maven" # Change to "gradle" if you use Gradle
LOG_FILE="log/build.log"

# Function to build the application
build_application() {
    echo "Building Spring Boot application..."
    cd "$APP_DIR" || exit 1

    if [ "$BUILD_TOOL" == "maven" ]; then
        mvn clean install > "$LOG_FILE" 2>&1
    elif [ "$BUILD_TOOL" == "gradle" ]; then
        ./gradlew clean build > "$LOG_FILE" 2>&1
    else
        echo "Unknown build tool: $BUILD_TOOL. Exiting."
        exit 1
    fi

    if [ $? -eq 0 ]; then
        echo "Application built successfully."
    else
        echo "Build failed. Check $LOG_FILE for details."
        exit 1
    fi
}

# Main Script Execution
build_application

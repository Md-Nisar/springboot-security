#!/bin/bash

# Variables
APP_NAME="springboot-security"
JAR_FILE="springboot-security-0.0.1-SNAPSHOT.jar"
LOG_FILE="app.log"
PID_FILE="app.pid"
APPLICATION_PROPERTIES_PATH="src/main/resources/application.properties"
EXTERNAL_APPLICATION_PROPERTIES_PATH="application.properties"

# Check if the application is already running
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null; then
        echo "Application is already running with PID: $PID"
        exit 1
    else
        echo "PID file exists but the application is not running. Removing stale PID file."
        rm -f "$PID_FILE"
    fi
fi

# Start the application
echo "Starting $APP_NAME..."
nohup java -jar --spring.config.location="$APPLICATION_PROPERTIES_PATH" "$JAR_FILE" > "$LOG_FILE" 2>&1 &

# Save the PID to a file
echo $! > "$PID_FILE"
echo "$APP_NAME started with PID: $!"

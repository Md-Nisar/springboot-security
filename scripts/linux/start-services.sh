#!/bin/bash

# Variables
MYSQL_SERVICE="mysql"
REDIS_SERVICE="redis-server"

# Function to check and start MySQL
check_and_start_mysql() {
    echo "Checking MySQL service..."
    if systemctl is-active --quiet "$MYSQL_SERVICE"; then
        echo "MySQL is already running."
    else
        echo "MySQL is not running. Starting MySQL..."
        sudo systemctl start "$MYSQL_SERVICE"
        if systemctl is-active --quiet "$MYSQL_SERVICE"; then
            echo "MySQL started successfully."
        else
            echo "Failed to start MySQL. Exiting."
            exit 1
        fi
    fi
}

# Function to check and start Redis
check_and_start_redis() {
    echo "Checking Redis service..."
    if systemctl is-active --quiet "$REDIS_SERVICE"; then
        echo "Redis is already running."
    else
        echo "Redis is not running. Starting Redis..."
        sudo systemctl start "$REDIS_SERVICE"
        if systemctl is-active --quiet "$REDIS_SERVICE"; then
            echo "Redis started successfully."
        else
            echo "Failed to start Redis. Exiting."
            exit 1
        fi
    fi
}

# Main Script Execution
check_and_start_mysql
check_and_start_redis

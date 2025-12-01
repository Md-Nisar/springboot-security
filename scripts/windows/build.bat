@echo off

:: Variables
set APP_DIR=D:\Projects\SPRING-BOOT\springboot-security
set BUILD_TOOL=maven
set LOG_FILE=logs\build.log

:: Build the application
echo Building application...
cd %APP_DIR%
if "%BUILD_TOOL%"=="maven" (
    mvn clean install -T 1C > %LOG_FILE% 2>&1
) else if "%BUILD_TOOL%"=="gradle" (
    gradlew clean build > %LOG_FILE% 2>&1
) else (
    echo Unknown build tool: %BUILD_TOOL%. Exiting.
    exit /b 1
)

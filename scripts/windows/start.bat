@echo off

:: Variables
set APP_NAME=springboot-security
set JAR_FILE=target/springboot-security-0.0.1-SNAPSHOT.jar
set LOG_FILE=logs/app.log
set PID_FILE=app.pid
set APPLICATION_PROPERTIES_PATH=src/main/resources/application.properties
set EXTERNAL_APPLICATION_PROPERTIES_PATH=application.properties

:: Start the application
echo Starting %APP_NAME%...
start /B java -jar -Dspring.config.location=%APPLICATION_PROPERTIES_PATH% %JAR_FILE% > %LOG_FILE% 2>&1


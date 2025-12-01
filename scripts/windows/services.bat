@echo off

:: Variables
set MYSQL_SERVICE=mysql
set REDIS_SERVICE=redis

:: Check and start MySQL
echo Checking MySQL service...
sc query %MYSQL_SERVICE% | find "RUNNING" > nul
if %ERRORLEVEL% equ 0 (
    echo MySQL is already running.
) else (
    echo MySQL is not running. Starting MySQL...
    net start %MYSQL_SERVICE%
    if %ERRORLEVEL% equ 0 (
        echo MySQL started successfully.
    ) else (
        echo Failed to start MySQL. Exiting.
        exit /b 1
    )
)

:: Check and start Redis
echo Checking Redis service...
sc query %REDIS_SERVICE% | find "RUNNING" > nul
if %ERRORLEVEL% equ 0 (
    echo Redis is already running.
) else (
    echo Redis is not running. Starting Redis...
    net start %REDIS_SERVICE%
    if %ERRORLEVEL% equ 0 (
        echo Redis started successfully.
    ) else (
        echo Failed to start Redis. Exiting.
        exit /b 1
    )
)

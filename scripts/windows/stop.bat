@echo off

:: Set the path to your PID file
set PID_FILE=app.pid

:: Check if the PID file exists
if not exist "%PID_FILE%" (
    echo PID file not found. The application may not be running.
    exit /b 1
)

:: Read the PID from the file
set /p PID=<%PID_FILE%
echo Found PID: %PID%

:: Check if the process is running
tasklist /FI "PID eq %PID%" | find "%PID%" >nul
if %ERRORLEVEL% neq 0 (
    echo No process found with PID: %PID%. The application may not be running.
    del "%PID_FILE%"
    exit /b 1
)

:: Stop the application
echo Stopping the application with PID: %PID%...
taskkill /F /PID %PID%

:: Delete the PID file
if exist "%PID_FILE%" (
    del "%PID_FILE%"
    echo PID file deleted.
)

echo Application stopped successfully.
exit /b 0
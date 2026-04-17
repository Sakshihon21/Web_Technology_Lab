@echo off
title Spring Boot - Practical 9 - Employee Task Management System
color 0B
echo.
echo  ====================================================
echo    Employee Task Management System  --  Practical 9
echo    Engineering Department, College Faculty Tracker
echo  ====================================================
echo.

REM ── Step 1: Check Java ───────────────────────────────────────────────────
echo [1/3] Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo  ERROR: Java is not installed or not found in PATH.
    echo  Please install Java 17 from: https://adoptium.net
    echo.
    pause
    exit /b 1
)
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    echo  Java found: %%v
)
echo.

REM ── Step 2: Setup Maven ──────────────────────────────────────────────────
echo [2/3] Checking Maven...

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%~dp0tools\apache-maven-%MAVEN_VERSION%
set MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MVN_CMD%" (
    echo  Maven already available at: %MAVEN_HOME%
    goto :run
)

REM Check system Maven
mvn -version >nul 2>&1
if %errorlevel% == 0 (
    echo  System Maven found.
    set MVN_CMD=mvn
    goto :run
)

echo  Maven not found. Downloading Maven %MAVEN_VERSION% automatically...
echo  (One-time download ~10 MB)
echo.
if not exist "%~dp0tools" mkdir "%~dp0tools"

powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $url = 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip'; $out = '%~dp0tools\maven.zip'; Write-Host '  Downloading Maven...'; Invoke-WebRequest -Uri $url -OutFile $out; Write-Host '  Extracting...'; Expand-Archive -Path $out -DestinationPath '%~dp0tools' -Force; Remove-Item $out; Write-Host '  Maven ready!'"

if not exist "%MVN_CMD%" (
    color 0C
    echo.
    echo  ERROR: Maven download failed. Check your internet connection.
    echo  Or install Maven manually: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)
echo  Maven ready!
echo.

:run
REM ── Step 3: Launch Spring Boot ───────────────────────────────────────────
echo [3/3] Starting Employee Task Management System...
echo.
echo  ---------------------------------------------------------------
echo   Once started, open your browser and visit:
echo.
echo     http://localhost:8080          (Web UI Dashboard)
echo     http://localhost:8080/h2-console  (H2 Database Console)
echo.
echo   REST API Endpoints:
echo     GET  /api/employees            - List all faculty
echo     POST /api/employees            - Add new faculty
echo     GET  /api/tasks                - List all tasks
echo     POST /api/tasks?employeeId=1   - Assign task
echo     GET  /api/tasks/stats          - Dashboard statistics
echo     GET  /api/tasks/overdue        - Overdue tasks
echo  ---------------------------------------------------------------
echo.

if "%MVN_CMD%"=="mvn" (
    mvn spring-boot:run
) else (
    "%MVN_CMD%" spring-boot:run
)

pause

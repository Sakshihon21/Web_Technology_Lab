@echo off
setlocal
title Spring Boot Hello World Web Service
color 0b

echo ========================================================
echo     Starting Spring Boot Hello-World Application
echo ========================================================
echo.

set MAVEN_DIR=%~dp0.maven
set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%MAVEN_DIR%\apache-maven-%MAVEN_VERSION%
set MVN_CMD="%MAVEN_HOME%\bin\mvn.cmd"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo [INFO] Maven not found. Downloading Apache Maven %MAVEN_VERSION% locally...
    if not exist "%MAVEN_DIR%" mkdir "%MAVEN_DIR%"
    curl -k -# -o "%MAVEN_DIR%\maven.zip" https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip
    
    echo [INFO] Extracting Maven...
    tar -xf "%MAVEN_DIR%\maven.zip" -C "%MAVEN_DIR%"
    
    if exist "%MAVEN_DIR%\maven.zip" del "%MAVEN_DIR%\maven.zip"
    echo [INFO] Maven installed successfully.
    echo.
)

echo [INFO] Launching the Spring Boot server...
echo.

call %MVN_CMD% spring-boot:run

pause

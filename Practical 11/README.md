# Employee Task Management System - Build Automation

This project demonstrates how to configure and manage the build lifecycle of a Spring Boot application using both **Maven** and **Gradle**.

## Overview
This is a simple Spring Boot application that manages "Employee Tasks". It includes a domain model, a REST controller, and a basic test. The primary goal of this repository is to demonstrate that the *same* source code can be seamlessly built and managed using two different industry-standard build tools.

---

## 1. Maven Configuration

The Maven build system is configured via the `pom.xml` file. It relies on the Spring Boot Starter Parent to inherit default plugin configurations, dependency management, and versioning.

### Build Lifecycle Commands (Maven Wrapper)

You can run these commands directly in your terminal/command prompt. The `mvnw` wrapper will automatically download the correct version of Maven for you.

- **Clean the project**: Removes the `target/` directory and previously compiled files.
  ```powershell
  .\mvnw clean
  ```
- **Compile the project**: Compiles the Java source files in `src/main/java`.
  ```powershell
  .\mvnw compile
  ```
- **Run Tests**: Executes the unit tests in `src/test/java`.
  ```powershell
  .\mvnw test
  ```
- **Package the Application**: Packages the compiled code into an executable JAR file (`target/employee-task-system-0.0.1-SNAPSHOT.jar`).
  ```powershell
  .\mvnw package
  ```
- **Run the Application**: Starts the Spring Boot server.
  ```powershell
  .\mvnw spring-boot:run
  ```

---

## 2. Gradle Configuration

The Gradle build system is configured via the `build.gradle` and `settings.gradle` files. It uses plugins for Java, Spring Boot, and Dependency Management.

### Build Lifecycle Commands (Gradle Wrapper)

You can run these commands directly in your terminal/command prompt. The `gradlew` wrapper will automatically download the correct version of Gradle for you.

- **Clean the project**: Removes the `build/` directory and previously compiled files.
  ```powershell
  .\gradlew clean
  ```
- **Compile the project**: Compiles the Java source files.
  ```powershell
  .\gradlew classes
  ```
- **Run Tests**: Executes the unit tests and reports results.
  ```powershell
  .\gradlew test
  ```
- **Package the Application**: Builds the executable JAR file (`build/libs/employee-task-system-0.0.1-SNAPSHOT.jar`).
  ```powershell
  .\gradlew bootJar
  ```
  *(or simply `.\gradlew build`, which includes running tests and assembling jars)*
- **Run the Application**: Starts the Spring Boot server.
  ```powershell
  .\gradlew bootRun
  ```

---

## API Endpoints

Once the application is running (by either Maven or Gradle, port 8080 by default), you can access the following REST endpoint:

- **GET /api/tasks** : Returns a list of tasks.
  URL: `http://localhost:8080/api/tasks`

## Project Structure

```
.
├── build.gradle                            # Gradle build configuration
├── pom.xml                                 # Maven build configuration
├── settings.gradle                         # Gradle settings
├── mvnw.cmd / gradlew.bat                  # Windows Wrappers
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/college/emp/
│   │           ├── EmployeeTaskApplication.java    # Main Entry Point
│   │           ├── controller/
│   │           │   └── TaskController.java         # REST Endpoints
│   │           └── model/
│   │               └── Task.java                   # Domain Model
│   └── test/
│       └── java/
│           └── com/college/emp/
│               └── EmployeeTaskApplicationTests.java # Unit Tests
└── README.md
```

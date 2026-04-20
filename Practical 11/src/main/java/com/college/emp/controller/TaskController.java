package com.college.emp.controller;

import com.college.emp.model.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @GetMapping
    public List<Task> getAllTasks() {
        return Arrays.asList(
            new Task(1L, "Configure Maven", "Setup pom.xml for project build", "COMPLETED"),
            new Task(2L, "Configure Gradle", "Setup build.gradle for project build", "COMPLETED"),
            new Task(3L, "Implement REST Controller", "Create endpoints for tasks", "IN_PROGRESS")
        );
    }
}

package com.wtl.taskmanagement.controller;

import com.wtl.taskmanagement.model.Task;
import com.wtl.taskmanagement.model.Task.TaskStatus;
import com.wtl.taskmanagement.model.Task.TaskType;
import com.wtl.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Task CRUD operations.
 * Base URL: /api/tasks
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // ── POST /api/tasks?employeeId=  →  Assign Task to Employee ─────────────
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task,
                                         @RequestParam Long employeeId) {
        try {
            Task created = taskService.createTask(task, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        }
    }

    // ── GET /api/tasks  →  Get All Tasks ─────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // ── GET /api/tasks/{id}  →  Get Task by ID ───────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        }
    }

    // ── GET /api/tasks/employee/{employeeId}  →  Tasks for an Employee ───────
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getTasksByEmployee(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(taskService.getTasksByEmployee(employeeId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        }
    }

    // ── GET /api/tasks/status/{status}  →  Filter by Status ─────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    // ── GET /api/tasks/type/{type}  →  Filter by Task Type ───────────────────
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Task>> getTasksByType(@PathVariable TaskType type) {
        return ResponseEntity.ok(taskService.getTasksByType(type));
    }

    // ── GET /api/tasks/overdue  →  Get Overdue Tasks ─────────────────────────
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    // ── GET /api/tasks/due-soon?days=7  →  Due within N days ─────────────────
    @GetMapping("/due-soon")
    public ResponseEntity<List<Task>> getTasksDueSoon(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(taskService.getTasksDueSoon(days));
    }

    // ── GET /api/tasks/search?keyword=  →  Search Tasks ──────────────────────
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        return ResponseEntity.ok(taskService.searchTasksByTitle(keyword));
    }

    // ── GET /api/tasks/stats  →  Dashboard Statistics ────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        return ResponseEntity.ok(taskService.getTaskStatistics());
    }

    // ── PUT /api/tasks/{id}?employeeId=  →  Update Task ─────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                         @Valid @RequestBody Task task,
                                         @RequestParam(required = false) Long employeeId) {
        try {
            Task updated = taskService.updateTask(id, task, employeeId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        }
    }

    // ── PATCH /api/tasks/{id}/status  →  Update Status Only ─────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id,
                                               @RequestParam TaskStatus status) {
        try {
            Task updated = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        }
    }

    // ── DELETE /api/tasks/{id}  →  Delete Task ────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(successResponse("Task deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        }
    }

    // ── Helper Methods ────────────────────────────────────────────────────────

    private Map<String, String> errorResponse(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("error", message);
        return res;
    }

    private Map<String, String> successResponse(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        return res;
    }
}

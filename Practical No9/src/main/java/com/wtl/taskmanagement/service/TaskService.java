package com.wtl.taskmanagement.service;

import com.wtl.taskmanagement.model.Employee;
import com.wtl.taskmanagement.model.Task;
import com.wtl.taskmanagement.model.Task.TaskStatus;
import com.wtl.taskmanagement.model.Task.TaskType;
import com.wtl.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer for Task business logic.
 */
@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeService employeeService;

    // ── CREATE ──────────────────────────────────────────────────────────────

    public Task createTask(Task task, Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        task.setEmployee(employee);
        task.setAssignedDate(LocalDate.now());
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        return taskRepository.save(task);
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByEmployee(Long employeeId) {
        // Verify employee exists first
        employeeService.getEmployeeById(employeeId);
        return taskRepository.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByType(TaskType type) {
        return taskRepository.findByTaskType(type);
    }

    @Transactional(readOnly = true)
    public List<Task> getOverdueTasks() {
        // Also update status to OVERDUE automatically
        List<Task> overdue = taskRepository.findOverdueTasks(LocalDate.now());
        overdue.forEach(t -> {
            if (t.getStatus() == TaskStatus.PENDING || t.getStatus() == TaskStatus.IN_PROGRESS) {
                t.setStatus(TaskStatus.OVERDUE);
                taskRepository.save(t);
            }
        });
        return overdue;
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksDueSoon(int days) {
        return taskRepository.findTasksDueSoon(LocalDate.now(), LocalDate.now().plusDays(days));
    }

    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // ── Dashboard Statistics ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Map<String, Long> getTaskStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total",       taskRepository.count());
        stats.put("pending",     taskRepository.countByStatus(TaskStatus.PENDING));
        stats.put("inProgress",  taskRepository.countByStatus(TaskStatus.IN_PROGRESS));
        stats.put("completed",   taskRepository.countByStatus(TaskStatus.COMPLETED));
        stats.put("overdue",     taskRepository.countByStatus(TaskStatus.OVERDUE));
        stats.put("cancelled",   taskRepository.countByStatus(TaskStatus.CANCELLED));
        stats.put("employees",   employeeService.getTotalEmployeeCount());
        return stats;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public Task updateTask(Long id, Task updatedTask, Long employeeId) {
        Task existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setTaskType(updatedTask.getTaskType());
        existing.setStatus(updatedTask.getStatus());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setPriority(updatedTask.getPriority());

        // Reassign to a different employee if needed
        if (employeeId != null && !existing.getEmployee().getId().equals(employeeId)) {
            Employee newEmployee = employeeService.getEmployeeById(employeeId);
            existing.setEmployee(newEmployee);
        }

        return taskRepository.save(existing);
    }

    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = getTaskById(id);
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}

package com.wtl.taskmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Task entity representing a duty/assignment
 * assigned to a faculty member.
 *
 * Task types: EXAM_DUTY, PROJECT_GUIDE, DOCUMENTATION,
 *             EVENT_COORDINATION, REPORT_SUBMISSION, OTHER
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Task type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @NotNull(message = "Task status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @NotNull(message = "Due date is required")
    @Column(nullable = false)
    private LocalDate dueDate;

    @Column
    private LocalDate assignedDate = LocalDate.now();

    @Column
    private String priority = "MEDIUM";  // LOW, MEDIUM, HIGH

    // Many tasks belong to one employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // ── Enums ────────────────────────────────────────────────────────────────

    public enum TaskType {
        EXAM_DUTY,
        PROJECT_GUIDE,
        DOCUMENTATION,
        EVENT_COORDINATION,
        REPORT_SUBMISSION,
        OTHER
    }

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        OVERDUE,
        CANCELLED
    }

    // ── Constructors ─────────────────────────────────────────────────────────

    public Task() {}

    public Task(String title, String description, TaskType taskType, TaskStatus status,
                LocalDate dueDate, String priority, Employee employee) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.status = status;
        this.dueDate = dueDate;
        this.priority = priority;
        this.employee = employee;
        this.assignedDate = LocalDate.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', type=" + taskType + ", status=" + status + "}";
    }
}

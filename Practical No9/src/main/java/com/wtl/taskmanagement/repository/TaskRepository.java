package com.wtl.taskmanagement.repository;

import com.wtl.taskmanagement.model.Task;
import com.wtl.taskmanagement.model.Task.TaskStatus;
import com.wtl.taskmanagement.model.Task.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Task CRUD operations.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // All tasks for a specific employee
    List<Task> findByEmployeeId(Long employeeId);

    // Tasks by status
    List<Task> findByStatus(TaskStatus status);

    // Tasks by type
    List<Task> findByTaskType(TaskType taskType);

    // Tasks by priority
    List<Task> findByPriority(String priority);

    // Tasks that are overdue (due date passed but not completed or cancelled)
    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);

    // Tasks due within next N days
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :today AND :deadline AND t.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Task> findTasksDueSoon(@Param("today") LocalDate today, @Param("deadline") LocalDate deadline);

    // Tasks for an employee by status
    List<Task> findByEmployeeIdAndStatus(Long employeeId, TaskStatus status);

    // Count tasks by status (for dashboard statistics)
    long countByStatus(TaskStatus status);

    // Count tasks by type
    long countByTaskType(TaskType taskType);

    // Search tasks by title
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    // All tasks for employees in a department
    @Query("SELECT t FROM Task t WHERE t.employee.department = :dept")
    List<Task> findByEmployeeDepartment(@Param("dept") String department);
}

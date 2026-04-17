package com.wtl.taskmanagement.repository;

import com.wtl.taskmanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee CRUD operations.
 * Spring Data JPA auto-implements all standard methods.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employee by email
    Optional<Employee> findByEmail(String email);

    // Find employees by department
    List<Employee> findByDepartmentIgnoreCase(String department);

    // Find employees by designation
    List<Employee> findByDesignationIgnoreCase(String designation);

    // Search employees by name (partial match)
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Check if email already exists (for validation)
    boolean existsByEmail(String email);

    // Get all distinct department names
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();
}

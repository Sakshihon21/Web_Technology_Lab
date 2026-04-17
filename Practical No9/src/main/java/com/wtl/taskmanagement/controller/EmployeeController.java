package com.wtl.taskmanagement.controller;

import com.wtl.taskmanagement.model.Employee;
import com.wtl.taskmanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Employee CRUD operations.
 * Base URL: /api/employees
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ── POST /api/employees  →  Create Employee ──────────────────────────────
    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee created = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
        }
    }

    // ── GET /api/employees  →  Get All Employees ─────────────────────────────
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // ── GET /api/employees/{id}  →  Get Employee by ID ───────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        }
    }

    // ── GET /api/employees/department/{dept}  →  Filter by Department ─────────
    @GetMapping("/department/{dept}")
    public ResponseEntity<List<Employee>> getByDepartment(@PathVariable String dept) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(dept));
    }

    // ── GET /api/employees/search?name=  →  Search by Name ───────────────────
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }

    // ── GET /api/employees/departments  →  List All Departments ──────────────
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        return ResponseEntity.ok(employeeService.getAllDepartments());
    }

    // ── PUT /api/employees/{id}  →  Update Employee ──────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                             @Valid @RequestBody Employee employee) {
        try {
            Employee updated = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        }
    }

    // ── DELETE /api/employees/{id}  →  Delete Employee ───────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(successResponse("Employee deleted successfully."));
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

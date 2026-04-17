package com.wtl.taskmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee entity representing a faculty member
 * in the Engineering Department.
 */
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Designation is required")
    @Column(nullable = false)
    private String designation;   // e.g. Professor, Associate Professor, Assistant Professor

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;    // e.g. Computer Science, IT, Electronics

    @Column
    private String phone;

    @Column
    private String expertise;     // Subjects / area of expertise

    // One employee can have many tasks
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    // ── Constructors ────────────────────────────────────────────────────────

    public Employee() {}

    public Employee(String name, String email, String designation, String department, String phone, String expertise) {
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.department = department;
        this.phone = phone;
        this.expertise = expertise;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', designation='" + designation + "', department='" + department + "'}";
    }
}

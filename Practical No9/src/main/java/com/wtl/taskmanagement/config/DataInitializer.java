package com.wtl.taskmanagement.config;

import com.wtl.taskmanagement.model.Employee;
import com.wtl.taskmanagement.model.Task;
import com.wtl.taskmanagement.model.Task.TaskStatus;
import com.wtl.taskmanagement.model.Task.TaskType;
import com.wtl.taskmanagement.repository.EmployeeRepository;
import com.wtl.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds the H2 in-memory database with sample faculty
 * and task data on every application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n========================================");
        System.out.println("  Loading sample data for Task Manager");
        System.out.println("========================================\n");

        // ── Create Faculty Employees ─────────────────────────────────────────
        Employee e1 = employeeRepository.save(new Employee(
                "Dr. Anjali Sharma",
                "anjali.sharma@college.edu",
                "Professor",
                "Computer Science",
                "9876543210",
                "Machine Learning, Data Structures"
        ));

        Employee e2 = employeeRepository.save(new Employee(
                "Prof. Rahul Patil",
                "rahul.patil@college.edu",
                "Associate Professor",
                "Computer Science",
                "9823456789",
                "Web Technologies, Database Management"
        ));

        Employee e3 = employeeRepository.save(new Employee(
                "Dr. Priya Kulkarni",
                "priya.kulkarni@college.edu",
                "Assistant Professor",
                "Information Technology",
                "9765432100",
                "Computer Networks, Cyber Security"
        ));

        Employee e4 = employeeRepository.save(new Employee(
                "Prof. Suresh Nair",
                "suresh.nair@college.edu",
                "Associate Professor",
                "Electronics",
                "9900112233",
                "Embedded Systems, VLSI Design"
        ));

        Employee e5 = employeeRepository.save(new Employee(
                "Dr. Meena Joshi",
                "meena.joshi@college.edu",
                "Professor",
                "Information Technology",
                "9812345678",
                "Artificial Intelligence, Cloud Computing"
        ));

        // ── Assign Tasks ─────────────────────────────────────────────────────
        taskRepository.save(new Task(
                "May Semester End Exam Duty",
                "Invigilate the semester end examinations for B.Tech 3rd year students in Hall A.",
                TaskType.EXAM_DUTY, TaskStatus.PENDING,
                LocalDate.now().plusDays(10), "HIGH", e1
        ));

        taskRepository.save(new Task(
                "Project Guide - AI Group 4",
                "Guide 4 final year students on their AI-based project: Smart Campus Monitoring System.",
                TaskType.PROJECT_GUIDE, TaskStatus.IN_PROGRESS,
                LocalDate.now().plusDays(45), "MEDIUM", e1
        ));

        taskRepository.save(new Task(
                "NBA Accreditation Documentation",
                "Prepare and compile course outcome mapping and attainment documents for NBA accreditation visit.",
                TaskType.DOCUMENTATION, TaskStatus.IN_PROGRESS,
                LocalDate.now().plusDays(5), "HIGH", e2
        ));

        taskRepository.save(new Task(
                "TechFest 2025 Coordination",
                "Coordinate technical event scheduling, team registrations, and venue arrangements for annual TechFest.",
                TaskType.EVENT_COORDINATION, TaskStatus.PENDING,
                LocalDate.now().plusDays(20), "HIGH", e2
        ));

        taskRepository.save(new Task(
                "Internal Marks Report Submission",
                "Submit consolidated internal assessment marks for all subjects to the examination cell.",
                TaskType.REPORT_SUBMISSION, TaskStatus.COMPLETED,
                LocalDate.now().minusDays(3), "MEDIUM", e3
        ));

        taskRepository.save(new Task(
                "Network Lab Setup Documentation",
                "Document the new network lab configuration, IP scheme, and equipment list for department records.",
                TaskType.DOCUMENTATION, TaskStatus.PENDING,
                LocalDate.now().plusDays(7), "LOW", e3
        ));

        taskRepository.save(new Task(
                "Practical Exam Invigilation - EC Dept",
                "Conduct and supervise practical examination for 2nd year Electronics students.",
                TaskType.EXAM_DUTY, TaskStatus.PENDING,
                LocalDate.now().plusDays(3), "HIGH", e4
        ));

        taskRepository.save(new Task(
                "IQAC Annual Report Preparation",
                "Compile yearly IQAC department report covering research, publications, and events.",
                TaskType.REPORT_SUBMISSION, TaskStatus.IN_PROGRESS,
                LocalDate.now().minusDays(1), "HIGH", e5
        ));

        taskRepository.save(new Task(
                "AI Workshop Guest Lecture Coordination",
                "Arrange and coordinate the 2-day workshop on Generative AI with industry experts.",
                TaskType.EVENT_COORDINATION, TaskStatus.COMPLETED,
                LocalDate.now().minusDays(10), "MEDIUM", e5
        ));

        taskRepository.save(new Task(
                "Project Guide - Cloud Computing Group",
                "Mentor 3 students developing a multi-cloud cost optimizer tool for their final year project.",
                TaskType.PROJECT_GUIDE, TaskStatus.PENDING,
                LocalDate.now().plusDays(60), "LOW", e5
        ));

        System.out.println("  ✓ 5 faculty members created.");
        System.out.println("  ✓ 10 tasks assigned.");
        System.out.println("\n  Application ready at: http://localhost:8080\n");
    }
}

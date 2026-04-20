package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/greeting")
    public Map<String, String> greeting() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello-World");
        response.put("description", "Welcome to the Spring Boot Foundational Web Service!");
        return response;
    }
}

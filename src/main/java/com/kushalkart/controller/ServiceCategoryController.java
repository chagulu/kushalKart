package com.kushalkart.controller;

import com.kushalkart.service.ServiceCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class ServiceCategoryController {

    private final ServiceCategoryService service;

    public ServiceCategoryController(ServiceCategoryService service) {
        this.service = service;
        this.service.seedDefaultServices(); // Optional: seed at startup
    }

    @GetMapping
    public ResponseEntity<?> getServices() {
        return ResponseEntity.ok(
            service.getAllServices()
        );
    }
}

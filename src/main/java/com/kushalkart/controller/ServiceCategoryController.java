package com.kushalkart.controller;

import com.kushalkart.dto.ServiceCategoryDetailsDTO;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.repository.ServiceCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceCategoryController {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryController(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    // GET /api/services - returns all service categories
    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllServices() {
        List<ServiceCategory> services = repository.findAll();
        return ResponseEntity.ok(services);
    }

    // GET /api/services/{id} - returns service by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        Optional<ServiceCategory> serviceOpt = repository.findById(id);

        if (serviceOpt.isPresent()) {
            ServiceCategory service = serviceOpt.get();

            // Create and return DTO
            ServiceCategoryDetailsDTO dto = new ServiceCategoryDetailsDTO(
                    service.getId(),
                    service.getName(),
                    service.getName() // assuming categoryName same as name
            );

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body("Service with id " + id + " not found.");
        }
    }
}

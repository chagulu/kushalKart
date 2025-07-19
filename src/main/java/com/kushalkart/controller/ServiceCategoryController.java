package com.kushalkart.controller;


import com.kushalkart.dto.ServiceCategoryDetailsDTO;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.repository.ServiceCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceCategoryController {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryController(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        Optional<ServiceCategory> serviceOpt = repository.findById(id);

        if (serviceOpt.isPresent()) {
            ServiceCategory service = serviceOpt.get();

            // Assuming categoryName is same as name for now.
            ServiceCategoryDetailsDTO dto = new ServiceCategoryDetailsDTO(
                    service.getId(),
                    service.getName(),
                    service.getName()
            );

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body("Service with id " + id + " not found.");
        }
    }

}

package com.kushalkart.controller;

import com.kushalkart.dto.ServiceCategoryDetailsDTO;
import com.kushalkart.dto.ServiceCategoryWithWorkersDTO;
import com.kushalkart.dto.NearbyServiceRequest;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.repository.ServiceCategoryRepository;
import com.kushalkart.service.ServiceCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceCategoryController {

    private final ServiceCategoryRepository repository;
    private final ServiceCategoryService serviceCategoryService;

    public ServiceCategoryController(
            ServiceCategoryRepository repository,
            ServiceCategoryService serviceCategoryService
    ) {
        this.repository = repository;
        this.serviceCategoryService = serviceCategoryService;
    }

    // GET /api/services - returns all service categories
    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllServices() {
        List<ServiceCategory> services = repository.findAll();
        return ResponseEntity.ok(services);
    }

    // NEW: Get services filtered by authenticated user's location
    @GetMapping("/by-location")
    public ResponseEntity<List<ServiceCategoryWithWorkersDTO>> getServicesByUserLocation(
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        List<ServiceCategoryWithWorkersDTO> services = serviceCategoryService
                .getServicesByUserLocation(userId);
        return ResponseEntity.ok(services);
    }

    // NEW: Get services for specific user (admin use)
    @GetMapping("/user/{userId}/by-location")
    public ResponseEntity<List<ServiceCategoryWithWorkersDTO>> getServicesByUserLocation(
            @PathVariable Long userId
    ) {
        List<ServiceCategoryWithWorkersDTO> services = serviceCategoryService
                .getServicesByUserLocation(userId);
        return ResponseEntity.ok(services);
    }

    // NEW: Get services for nearby areas
    @PostMapping("/nearby")
    public ResponseEntity<List<ServiceCategoryWithWorkersDTO>> getServicesByNearbyLocation(
            @RequestBody NearbyServiceRequest request,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        List<ServiceCategoryWithWorkersDTO> services = serviceCategoryService
                .getServicesByNearbyLocation(userId, request.getPincodes());
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

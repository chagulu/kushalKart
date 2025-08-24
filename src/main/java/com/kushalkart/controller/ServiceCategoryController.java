package com.kushalkart.controller;

import com.kushalkart.dto.NearbyServiceRequest;
import com.kushalkart.dto.ServiceCategoryDetailsDTO;
import com.kushalkart.dto.ServiceCategoryWithWorkersDTO;
import com.kushalkart.dto.ServiceCategorySummaryDTO;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.repository.ServiceCategoryRepository;
import com.kushalkart.service.ServiceCategoryService;
import com.kushalkart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceCategoryController {

    private final ServiceCategoryRepository repository;
    private final ServiceCategoryService serviceCategoryService;
    private final UserService userService;

    public ServiceCategoryController(
            ServiceCategoryRepository repository,
            ServiceCategoryService serviceCategoryService,
            UserService userService
    ) {
        this.repository = repository;
        this.serviceCategoryService = serviceCategoryService;
        this.userService = userService;
    }

    // GET /api/services - returns all service categories
    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllServices() {
        List<ServiceCategory> services = repository.findAll();
        return ResponseEntity.ok(services);
    }

    // Get services filtered by authenticated user's location (old DTO)
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

    // Admin variant by userId (old DTO)
    @GetMapping("/user/{userId}/by-location")
    public ResponseEntity<List<ServiceCategoryWithWorkersDTO>> getServicesByUserLocation(
            @PathVariable Long userId
    ) {
        List<ServiceCategoryWithWorkersDTO> services = serviceCategoryService
                .getServicesByUserLocation(userId);
        return ResponseEntity.ok(services);
    }

    // Nearby areas (old DTO)
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

    // Enriched by user location (new DTO)
    @GetMapping("/by-location/enriched")
    public ResponseEntity<List<ServiceCategorySummaryDTO>> getServicesByUserLocationEnriched(
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        String pincode = resolveUserPincode(userId);
        List<ServiceCategorySummaryDTO> result = fetchSummariesByPincode(pincode);
        return ResponseEntity.ok(result);
    }

    // Enriched by userId (admin)
    @GetMapping("/user/{userId}/by-location/enriched")
    public ResponseEntity<List<ServiceCategorySummaryDTO>> getServicesByUserLocationEnrichedAdmin(
            @PathVariable Long userId
    ) {
        String pincode = resolveUserPincode(userId);
        List<ServiceCategorySummaryDTO> result = fetchSummariesByPincode(pincode);
        return ResponseEntity.ok(result);
    }

    // ✅ Updated: Enriched by authenticated user's pincode (no query param)
    @GetMapping("/by-pincode/enriched")
    public ResponseEntity<List<ServiceCategorySummaryDTO>> getServicesByPincodeEnriched(
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        String pincode = resolveUserPincode(userId);
        List<ServiceCategorySummaryDTO> result = fetchSummariesByPincode(pincode);
        return ResponseEntity.ok(result);
    }

    // GET /api/services/{id} - returns service by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        Optional<ServiceCategory> serviceOpt = repository.findById(id);

        if (serviceOpt.isPresent()) {
            ServiceCategory service = serviceOpt.get();

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

    // Internal helpers

    private String resolveUserPincode(Long userId) {
        return userService.findPrimaryAddressPincodeByUserId(userId);
    }

    private List<ServiceCategorySummaryDTO> fetchSummariesByPincode(String pincode) {
        List<Object[]> rows = repository.findServiceCategorySummariesByPincode(pincode);
        return rows.stream().map(this::mapSummaryRow).collect(Collectors.toList());
    }

    private ServiceCategorySummaryDTO mapSummaryRow(Object[] row) {
        Long categoryId = row[0] != null ? ((Number) row[0]).longValue() : null;
        String categoryName = (String) row[1];
        String userPincode = (String) row[2];
        long availableWorkersCount = row[3] != null ? ((Number) row[3]).longValue() : 0L;
        String description = (String) row[4];
        BigDecimal defaultRate = row[5] == null ? null :
                (row[5] instanceof BigDecimal ? (BigDecimal) row[5] : new BigDecimal(row[5].toString()));
        Double averageRating = row[6] != null ? ((Number) row[6]).doubleValue() : null;

        return new ServiceCategorySummaryDTO(
                categoryId, categoryName, userPincode, availableWorkersCount,
                description, defaultRate, averageRating
        );
    }
}

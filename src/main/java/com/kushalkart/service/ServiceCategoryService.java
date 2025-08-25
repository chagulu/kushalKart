package com.kushalkart.service;

import com.kushalkart.dto.ServiceCategoryWithWorkersDTO;
import com.kushalkart.dto.ServiceCategorySummaryDTO;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.entity.UserAddress;
import com.kushalkart.repository.ServiceCategoryRepository;
import com.kushalkart.repository.UserAddressRepository;
import com.kushalkart.repository.WorkerAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository repository;
    private final UserAddressRepository userAddressRepository;
    private final WorkerAddressRepository workerAddressRepository;

    public ServiceCategoryService(
            ServiceCategoryRepository repository,
            UserAddressRepository userAddressRepository,
            WorkerAddressRepository workerAddressRepository
    ) {
        this.repository = repository;
        this.userAddressRepository = userAddressRepository;
        this.workerAddressRepository = workerAddressRepository;
    }

    public List<ServiceCategory> getAllServices() {
        return repository.findAll();
    }

    // -------------------- Existing OLD flow (kept for backward compatibility) --------------------

    // OLD: Get service categories filtered by user's pincode (returns the old 4-field DTO)
    public List<ServiceCategoryWithWorkersDTO> getServicesByUserLocation(Long userId) {
        String userPincode = resolveUserPincode(userId);

        // 2) Find categories with workers in same pincode
        List<ServiceCategory> categories = repository.findCategoriesWithWorkersByPincode(userPincode);

        // 3) For each category, get the count of available workers (N+1 via address repo)
        return categories.stream()
                .map(category -> {
                    long workerCount = countWorkersInCategoryByPincode(category.getId(), userPincode);
                    return new ServiceCategoryWithWorkersDTO(
                            category.getId(),
                            category.getName(),
                            userPincode,
                            workerCount
                    );
                })
                .collect(Collectors.toList());
    }

    // OLD: Get service categories for nearby areas (multiple pincodes) — still returns old DTO
    public List<ServiceCategoryWithWorkersDTO> getServicesByNearbyLocation(
            Long userId,
            List<String> nearbyPincodes
    ) {
        String userPincode = resolveUserPincode(userId);

        // Include user's pincode in search
        List<String> allPincodes = new ArrayList<>(nearbyPincodes);
        if (!allPincodes.contains(userPincode)) {
            allPincodes.add(userPincode);
        }

        List<ServiceCategory> categories = repository.findCategoriesWithWorkersByPincodes(allPincodes);

        return categories.stream()
                .map(category -> {
                    long workerCount = countWorkersInCategoryByPincodes(category.getId(), allPincodes);
                    return new ServiceCategoryWithWorkersDTO(
                            category.getId(),
                            category.getName(),
                            userPincode,
                            workerCount
                    );
                })
                .collect(Collectors.toList());
    }

    // -------------------- New ENRICHED flow (use this in your controller to get extra fields) --------------------

    // ENRICHED: by userId (uses summary native query to fetch description, defaultRate, averageRating)
    public List<ServiceCategorySummaryDTO> getEnrichedServicesByUserLocation(Long userId) {
        String pincode = resolveUserPincode(userId);
        return getEnrichedServicesByPincode(pincode);
    }

    // ENRICHED: by explicit pincode (handy for testing)
    public List<ServiceCategorySummaryDTO> getEnrichedServicesByPincode(String pincode) {
        List<Object[]> rows = repository.findServiceCategorySummariesByPincode(pincode);
        return rows.stream().map(this::mapSummaryRow).collect(Collectors.toList());
    }

    // -------------------- Helpers --------------------

    private String resolveUserPincode(Long userId) {
        return userAddressRepository.findByUserId(userId)
                .map(UserAddress::getPincode)
                .orElseThrow(() ->
                        new EntityNotFoundException("No address found for user " + userId));
    }

    private long countWorkersInCategoryByPincode(Long categoryId, String pincode) {
        return workerAddressRepository.findByPincode(pincode).stream()
                .filter(wa -> wa.getWorker().getServiceCategoryId().equals(categoryId))
                .count();
    }

    private long countWorkersInCategoryByPincodes(Long categoryId, List<String> pincodes) {
        return pincodes.stream()
                .flatMap(pincode -> workerAddressRepository.findByPincode(pincode).stream())
                .filter(wa -> wa.getWorker().getServiceCategoryId().equals(categoryId))
                .distinct()
                .count();
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

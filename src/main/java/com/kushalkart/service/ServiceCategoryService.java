package com.kushalkart.service;

import com.kushalkart.dto.ServiceCategoryWithWorkersDTO;
import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.entity.UserAddress;
import com.kushalkart.repository.ServiceCategoryRepository;
import com.kushalkart.repository.UserAddressRepository;
import com.kushalkart.admin.repository.WorkerAddressRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;      // ← add this
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

    // NEW: Get service categories filtered by user's pincode
    public List<ServiceCategoryWithWorkersDTO> getServicesByUserLocation(Long userId) {
        // 1. Get user's pincode
        String userPincode = userAddressRepository.findByUserId(userId)
                .map(UserAddress::getPincode)
                .orElseThrow(() -> 
                    new EntityNotFoundException("No address found for user " + userId));

        // 2. Find categories with workers in same pincode
        List<ServiceCategory> categories = repository.findCategoriesWithWorkersByPincode(userPincode);

        // 3. For each category, get the count of available workers
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

    // NEW: Get service categories for nearby areas (multiple pincodes)
    public List<ServiceCategoryWithWorkersDTO> getServicesByNearbyLocation(
            Long userId, 
            List<String> nearbyPincodes
    ) {
        String userPincode = userAddressRepository.findByUserId(userId)
                .map(UserAddress::getPincode)
                .orElseThrow(() -> 
                    new EntityNotFoundException("No address found for user " + userId));

        // Include user's pincode in search
        List<String> allPincodes = new ArrayList<>(nearbyPincodes);
        allPincodes.add(userPincode);

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

    // Helper method to count workers in a category by pincode
    private long countWorkersInCategoryByPincode(Long categoryId, String pincode) {
        return workerAddressRepository.findByPincode(pincode).stream()
                .filter(wa -> wa.getWorker().getServiceCategoryId().equals(categoryId))
                .count();
    }

    // Helper method to count workers in a category by multiple pincodes
    private long countWorkersInCategoryByPincodes(Long categoryId, List<String> pincodes) {
        return pincodes.stream()
                .flatMap(pincode -> workerAddressRepository.findByPincode(pincode).stream())
                .filter(wa -> wa.getWorker().getServiceCategoryId().equals(categoryId))
                .distinct()
                .count();
    }

    public void seedDefaultServices() {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                newCategory("Labour"),
                newCategory("Rajmistri (Mason)"),
                newCategory("Small House Building Contractor"),
                newCategory("Electrician"),
                newCategory("Plumber"),
                newCategory("Carpenter"),
                newCategory("Painter"),
                newCategory("Welder"),
                newCategory("Tile/Marble Worker"),
                newCategory("Roofer"),
                newCategory("Cleaner / Housekeeping"),
                newCategory("Gardener"),
                newCategory("AC/Fridge Mechanic"),
                newCategory("Water Tank Cleaner")
            ));
        }
    }

    private ServiceCategory newCategory(String name) {
        ServiceCategory category = new ServiceCategory();
        category.setName(name);
        return category;
    }
}

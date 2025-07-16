package com.kushalkart.service;

import com.kushalkart.entity.ServiceCategory;
import com.kushalkart.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryService(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ServiceCategory> getAllServices() {
        return repository.findAll();
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

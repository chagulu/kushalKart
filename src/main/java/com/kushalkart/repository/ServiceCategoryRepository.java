package com.kushalkart.repository;

import com.kushalkart.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
    @Query("""
        SELECT DISTINCT sc FROM ServiceCategory sc 
        JOIN Worker w ON w.serviceCategoryId = sc.id 
        JOIN WorkerAddress wa ON wa.worker.id = w.id 
        WHERE wa.pincode = :pincode
        """)
    List<ServiceCategory> findCategoriesWithWorkersByPincode(@Param("pincode") String pincode);
    
    @Query("""
        SELECT DISTINCT sc FROM ServiceCategory sc 
        JOIN Worker w ON w.serviceCategoryId = sc.id 
        JOIN WorkerAddress wa ON wa.worker.id = w.id 
        WHERE wa.pincode IN :pincodes
        """)
    List<ServiceCategory> findCategoriesWithWorkersByPincodes(@Param("pincodes") List<String> pincodes);
    
    // Alternative native SQL queries if the above don't work due to entity mapping issues
    @Query(value = """
        SELECT DISTINCT sc.* FROM service_category sc 
        INNER JOIN workers w ON w.service_category_id = sc.id 
        INNER JOIN worker_addresses wa ON wa.worker_id = w.id 
        WHERE wa.pincode = :pincode
        """, nativeQuery = true)
    List<ServiceCategory> findCategoriesWithWorkersByPincodeNative(@Param("pincode") String pincode);
    
    @Query(value = """
        SELECT DISTINCT sc.* FROM service_category sc 
        INNER JOIN workers w ON w.service_category_id = sc.id 
        INNER JOIN worker_addresses wa ON wa.worker_id = w.id 
        WHERE wa.pincode IN (:pincodes)
        """, nativeQuery = true)
    List<ServiceCategory> findCategoriesWithWorkersByPincodesNative(@Param("pincodes") List<String> pincodes);
}

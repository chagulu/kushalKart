package com.kushalkart.repository;

import com.kushalkart.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    // Find categories that have at least one worker in the given pincode (JPQL, via entity mappings)
    @Query("""
        SELECT DISTINCT sc FROM ServiceCategory sc
        JOIN Worker w ON w.serviceCategoryId = sc.id
        JOIN WorkerAddress wa ON wa.worker.id = w.id
        WHERE wa.pincode = :pincode
        """)
    List<ServiceCategory> findCategoriesWithWorkersByPincode(@Param("pincode") String pincode);

    // Find categories that have at least one worker in any of the given pincodes (JPQL)
    @Query("""
        SELECT DISTINCT sc FROM ServiceCategory sc
        JOIN Worker w ON w.serviceCategoryId = sc.id
        JOIN WorkerAddress wa ON wa.worker.id = w.id
        WHERE wa.pincode IN :pincodes
        """)
    List<ServiceCategory> findCategoriesWithWorkersByPincodes(@Param("pincodes") List<String> pincodes);

    // Native alternative for single pincode if entity relationships cause issues
    @Query(value = """
        SELECT DISTINCT sc.* FROM service_category sc
        INNER JOIN workers w ON w.service_category_id = sc.id
        INNER JOIN worker_addresses wa ON wa.worker_id = w.id
        WHERE wa.pincode = :pincode
        """, nativeQuery = true)
    List<ServiceCategory> findCategoriesWithWorkersByPincodeNative(@Param("pincode") String pincode);

    // Native alternative for multiple pincodes
    @Query(value = """
        SELECT DISTINCT sc.* FROM service_category sc
        INNER JOIN workers w ON w.service_category_id = sc.id
        INNER JOIN worker_addresses wa ON wa.worker_id = w.id
        WHERE wa.pincode IN (:pincodes)
        """, nativeQuery = true)
    List<ServiceCategory> findCategoriesWithWorkersByPincodesNative(@Param("pincodes") List<String> pincodes);

    /*
     Enriched summary for "services by location":
     - categoryId, categoryName
     - userPincode (echoed)
     - availableWorkersCount: workers in that category and pincode
     - description, defaultRate from service_details
     - averageRating: AVG ratings for workers of that category in that pincode
    */
    @Query(value = """
        SELECT
            sc.id AS categoryId,
            sc.name AS categoryName,
            :pincode AS userPincode,
            COUNT(DISTINCT w.id) AS availableWorkersCount,
            sd.description AS description,
            sd.default_rate AS defaultRate,
            AVG(r.rating) AS averageRating
        FROM service_category sc
        JOIN service_details sd ON sd.service_category_id = sc.id
        LEFT JOIN workers w ON w.service_category_id = sc.id
        LEFT JOIN ratings r ON r.worker_id = w.id
        JOIN worker_addresses wa ON wa.worker_id = w.id
        WHERE wa.pincode = :pincode
        GROUP BY sc.id, sc.name, sd.description, sd.default_rate
        """, nativeQuery = true)
    List<Object[]> findServiceCategorySummariesByPincode(@Param("pincode") String pincode);

    // Detailed summary for a specific service and worker
    @Query(value = """
        SELECT
            sc.id AS categoryId,
            sc.name AS categoryName,
            :pincode AS userPincode,
            COUNT(DISTINCT w.id) AS availableWorkersCount,
            sd.description AS description,
            sd.default_rate AS defaultRate,
            AVG(r.rating) AS averageRating
        FROM service_category sc
        JOIN service_details sd ON sd.service_category_id = sc.id
        LEFT JOIN workers w ON w.service_category_id = sc.id
        LEFT JOIN ratings r ON r.worker_id = w.id
        JOIN worker_addresses wa ON wa.worker_id = w.id
        WHERE wa.pincode = :pincode
          AND sc.id = :serviceId
          AND w.id = :workerId
        GROUP BY sc.id, sc.name, sd.description, sd.default_rate
        """, nativeQuery = true)
    List<Object[]> findServiceCategorySummaryByPincodeAndServiceAndWorker(
            @Param("pincode") String pincode,
            @Param("serviceId") Long serviceId,
            @Param("workerId") Long workerId
    );
}

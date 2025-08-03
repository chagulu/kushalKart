package com.kushalkart.admin.repository;

import com.kushalkart.admin.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByMobile(String mobile);

    Optional<Worker> findByUsername(String username);

    Page<Worker> findByIdInAndServiceId(List<Long> ids, Long serviceId, Pageable pageable);

    Page<Worker> findByIdInAndServiceCategoryId(List<Long> ids, Long serviceCategoryId, Pageable pageable);
}


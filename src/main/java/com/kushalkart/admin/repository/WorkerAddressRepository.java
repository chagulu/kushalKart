package com.kushalkart.admin.repository;

import com.kushalkart.admin.entity.WorkerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerAddressRepository extends JpaRepository<WorkerAddress, Long> {
    Optional<WorkerAddress> findByWorkerId(Long workerId);
}

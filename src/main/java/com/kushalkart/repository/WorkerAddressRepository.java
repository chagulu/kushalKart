package com.kushalkart.repository;

import com.kushalkart.entity.WorkerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerAddressRepository extends JpaRepository<WorkerAddress, Long> {

    List<WorkerAddress> findByPincode(String pincode);

    Optional<WorkerAddress> findFirstByWorker_Id(Long workerId); // for first address

    Optional<WorkerAddress> findByWorker_Id(Long workerId); // for exact match
}

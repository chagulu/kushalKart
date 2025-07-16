package com.kushalkart.admin.repository;

import com.kushalkart.admin.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByMobile(String mobile);
}

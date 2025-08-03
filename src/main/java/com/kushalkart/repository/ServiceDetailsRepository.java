package com.kushalkart.repository;

import com.kushalkart.entity.ServiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDetailsRepository extends JpaRepository<ServiceDetails, Long> {
}

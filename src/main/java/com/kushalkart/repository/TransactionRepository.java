package com.kushalkart.repository;

import com.kushalkart.entity.MasterTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<MasterTransactionHistory, Long> {

    // Declare a method to find MasterTransactionHistory by bookingId
    MasterTransactionHistory findByBookingId(Long bookingId);
}


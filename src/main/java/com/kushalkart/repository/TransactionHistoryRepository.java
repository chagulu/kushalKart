package com.kushalkart.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<com.kushalkart.entity.MasterTransactionHistory, Long> {

    @Query(value = """
        SELECT 
            mth.id AS id,
            mth.user_id AS userId,
            mth.booking_id AS bookingId,
            bt.name AS balanceType,
            tt.name AS transactionType,
            pt.name AS paymentType,
            mth.amount AS amount,
            mth.remarks AS remarks,
            mth.transaction_reference AS transactionReference,
            ts.name AS transactionStatus,
            mth.created_at AS createdAt
        FROM master_transaction_history mth
        JOIN balance_type bt ON mth.balance_type_id = bt.id
        JOIN transaction_type tt ON mth.transaction_type_id = tt.id
        LEFT JOIN payment_type pt ON mth.payment_type_id = pt.id
        JOIN transaction_status ts ON mth.transaction_status_id = ts.id
        WHERE mth.user_id = :userId
        AND (:bookingId IS NULL OR mth.booking_id = :bookingId)
        ORDER BY mth.created_at DESC
        """, nativeQuery = true)
    List<Object[]> findTransactionsForUser(
            @Param("userId") Long userId,
            @Param("bookingId") Long bookingId
    );
}


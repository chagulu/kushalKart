package com.kushalkart.mapper;

import com.kushalkart.dto.TransactionHistoryDTO;

import java.sql.Timestamp;

public class TransactionHistoryMapper {
    public static TransactionHistoryDTO fromRow(Object[] row) {
        TransactionHistoryDTO dto = new TransactionHistoryDTO();
        dto.setId(((Number) row[0]).longValue());
        dto.setUserId(((Number) row[1]).longValue());
        dto.setBookingId(((Number) row[2]).longValue());
        dto.setBalanceType((String) row[3]);
        dto.setTransactionType((String) row[4]);
        dto.setPaymentType((String) row[5]);
        dto.setAmount((java.math.BigDecimal) row[6]);
        dto.setRemarks((String) row[7]);
        dto.setTransactionReference((String) row[8]);
        dto.setTransactionStatus((String) row[9]);
        dto.setCreatedAt(((Timestamp) row[10]).toLocalDateTime());
        return dto;
    }
}

package com.kushalkart.service;

import com.kushalkart.dto.TransactionHistoryDTO;
import com.kushalkart.mapper.TransactionHistoryMapper;
import com.kushalkart.repository.TransactionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionHistoryRepository repository;

    public TransactionService(TransactionHistoryRepository repository) {
        this.repository = repository;
    }

    // Save a transaction
    public com.kushalkart.entity.MasterTransactionHistory saveTransaction(
            com.kushalkart.entity.MasterTransactionHistory transaction) {
        return repository.save(transaction);
    }

    // ✅ Add this method for controller
    public List<TransactionHistoryDTO> getTransactionsByUserId(Long userId, int page, int size) {
        // Optional: implement pagination manually for native query
        int offset = page * size;
        List<Object[]> rows = repository.findTransactionsForUser(userId, null); // bookingId is optional
        // Apply pagination manually
        return rows.stream()
                .skip(offset)
                .limit(size)
                .map(TransactionHistoryMapper::fromRow)
                .collect(Collectors.toList());
    }
}

package com.kushalkart.controller;


import com.kushalkart.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.dto.TransactionHistoryDTO;

import java.util.List;




@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/mine")
    public ResponseEntity<?> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            List<TransactionHistoryDTO> transactions = transactionService.getTransactionsByUserId(userId, page, size);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Something went wrong: " + e.getMessage(), "error")
            );
        }
    }

    // Error response DTO
    public static class ErrorResponse {
        private String message;
        private String status;

        public ErrorResponse(String message, String status) {
            this.message = message;
            this.status = status;
        }
        public String getMessage() { return message; }
        public String getStatus() { return status; }
    }
}

